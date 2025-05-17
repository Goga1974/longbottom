package com.goga74.platform.controller;

import com.goga74.platform.DB.entity.jback.*;
import com.goga74.platform.DB.repository.*;
import com.goga74.platform.DB.dbservice.JbackDataService;
import com.goga74.platform.controller.dto.jback.Item;
import com.goga74.platform.controller.request.CreateRequest;
import com.goga74.platform.controller.request.LoginRequest;
import com.goga74.platform.controller.request.TransactionRequest;
import com.goga74.platform.controller.request.UnlockRequest;
import com.goga74.platform.controller.response.jback.JbackCommonResponse;
import com.goga74.platform.service.PinService;
import com.goga74.platform.util.JsonUtil;
import com.goga74.platform.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api")
public class JbackController {

    private final JbackDataService jbackDataService;
    private final JbackDataRepository JBackDataRepository;
    private final JbackItemRepository jbackItemRepository;
    private final JbackRequestLogRepository jbackRequestLogRepository;
    private final JbackUnlockedRepository jbackUnlockedRepository;
    private final JbackInstallRepository jbackInstallRepository;
    private final JWTUtil jwtUtil; // Добавляем JwtUtil

    @Value("${request.limit.minutes}")
    private int requestLimitMinutes;

    public JbackController(JbackDataService jbackDataService,
                           JbackDataRepository JBackDataRepository,
                           JbackItemRepository jbackItemRepository,
                           JbackRequestLogRepository jbackRequestLogRepository,
                           JbackUnlockedRepository jbackUnlockedRepository,
                           JbackInstallRepository jbackInstallRepository,
                           JWTUtil jwtUtil) {
        this.jbackDataService = jbackDataService;
        this.JBackDataRepository = JBackDataRepository;
        this.jbackItemRepository = jbackItemRepository;
        this.jbackRequestLogRepository = jbackRequestLogRepository;
        this.jbackUnlockedRepository = jbackUnlockedRepository;
        this.jbackInstallRepository = jbackInstallRepository;
        this.jwtUtil = jwtUtil; // Инициализируем jwtUtil
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateRequest request, HttpServletRequest httpRequest)
    {
        Map<String, Object> response = new HashMap<>();
        if (request == null)
        {
            response.put("ERROR_MESSAGE", "request is null");
            return ResponseEntity.ok(response);
        }
        final String userId = request.getUserId();
        if (userId == null)
        {
            response.put("ERROR_MESSAGE", "userId is null");
            return ResponseEntity.ok(response);
        }
        final String installId = request.getUserId();
        String pin = request.getPin();
        if (pin == null || pin.isEmpty()) // create pin
        {

        }

        // Получение IP и User-Agent из HttpServletRequest
        final String ipAddress = httpRequest.getRemoteAddr();
        final String userAgent = httpRequest.getHeader("User-Agent");
        jbackDataService.logRequest(request.getUserId(), ipAddress, userAgent);

        // Проверка последнего запроса с этого IP
        /*
        Optional<RequestLog> lastRequestOpt = jbackRequestLogRepository.
                findFirstByIpAddressOrderByRequestTimeDesc(ipAddress);
        if (lastRequestOpt.isPresent())
        {
            LocalDateTime lastRequestTime = lastRequestOpt.get().getRequestTime();
            LocalDateTime gmtLocalDateTime = ZonedDateTime.now(ZoneId.of("GMT"))
                    .toLocalDateTime()
                    .minusMinutes(requestLimitMinutes);

            if (lastRequestTime.isAfter(gmtLocalDateTime))
            {
                response.put("ERROR_MESSAGE", "Request limit exceeded. Try again later.");
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
            }
        }
        */

        try
        {
            Optional<UserEntity> existingUser = JBackDataRepository.findById(request.getUserId());
            if (existingUser.isEmpty())
            {
                // new user
                saveUser(request, response, PinService.generatePin(), true);
            }
            if (existingUser.isPresent())
            {
                UserEntity exUser = existingUser.get();
                if (pin == null || pin.isEmpty() || !pin.equals(exUser.getPin()))
                {
                    response.put("ERROR_MESSAGE", "User already exists and pin does not match or pin is empty");
                    return ResponseEntity.ok(response);
                }
                else
                {
                    // update user
                    saveUser(request, response, exUser.getPin(), false);
                }
            }
            List<InstallEntity> installList = jbackInstallRepository.findByUserId(userId);
            if (installList.isEmpty())
            {
                InstallEntity install = new InstallEntity();
                install.setInstallId(installId);
                install.setUserId(userId);
                ZonedDateTime gmtTime = ZonedDateTime.now(ZoneId.of("GMT"));
                LocalDateTime gmtLocalDateTime = gmtTime.toLocalDateTime();
                install.setRequestTime(gmtLocalDateTime);
                jbackInstallRepository.save(install);
            }
            if (installList.size() == 1) // only one record
            {
                final String install = installList.get(0).getInstallId();
                if (install != null && !install.isEmpty() && !install.equals(installId))
                {
                    response.put("ERROR_MESSAGE", "Install id does not match the request");
                    return ResponseEntity.ok(response);
                }
            }
            if (installList.size() > 1)
            {
                // logic with multiply install records
                response.put("ERROR_MESSAGE", "More than one install found");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            response.put("ERROR_MESSAGE", "An error occurred: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    private void saveUser(CreateRequest request, Map<String, Object> response, final String pin, final boolean isCreate)
    {
        UserEntity user = new UserEntity();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        if (isCreate)
        {
            user.setPin(pin);
        }

        JBackDataRepository.save(user);
        response.put("pin", user.getPin());

        List<ItemEntity> items = request.getItems().stream()
                .map(item -> {
                    ItemEntity itemEntity = new ItemEntity();
                    itemEntity.setItemId(item.getItemId());
                    itemEntity.setUserId(request.getUserId());
                    itemEntity.setCount(item.getCount());
                    return itemEntity;
                })
                .toList();

        //dataService.saveItems(items);
        jbackItemRepository.saveAll(items);

        // Сохранение в таблицу unlocked
        if (!request.getUnlocked().isEmpty())
        {
            UnlockedEntity unlockedEntity = new UnlockedEntity();
            unlockedEntity.setItemId(request.getUnlocked().get(0).getItemId());
            unlockedEntity.setUserId(request.getUserId());
            unlockedEntity.setCount(1);
            jbackUnlockedRepository.save(unlockedEntity);
        }

        response.put("message", "User and items created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        if (request != null && request.getUserId() != null) {
            final String userId = request.getUserId();
            Map<String, Object> userData = jbackDataService.getUser(userId);

            if (userData.containsKey("ERROR_MESSAGE")) {
                response.put("ERROR_MESSAGE", userData.get("ERROR_MESSAGE"));
                return ResponseEntity.ok(response);
            }

            // Генерация JWT токена и добавление его в ответ
            /*
            final String token = jwtUtil.generateToken(userId);
            if (dataService.saveToken(userId, token) != null)
            {
                response.put("token", token);
            }
            */

            response.putAll(userData);
            return ResponseEntity.ok(response);
        }

        response.put("ERROR_MESSAGE", "userId is null");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        if (userId != null) {
            Map<String, Object> userData = jbackDataService.getUser(userId);

            if (userData.containsKey("ERROR_MESSAGE")) {
                response.put("ERROR_MESSAGE", userData.get("ERROR_MESSAGE"));
                return ResponseEntity.ok(response);
            }

            response.putAll(userData);
            return ResponseEntity.ok(response);
        }

        response.put("ERROR_MESSAGE", "userId is null");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction")
    public ResponseEntity<JbackCommonResponse> handleTransaction(@RequestBody TransactionRequest request)
    {
        Optional<UserEntity> userOptional = JBackDataRepository.findById(request.getUserId());

        if (userOptional.isPresent())
        {
            UserEntity user = userOptional.get();

            // Здесь может быть проверка pin, если это требуется
            // if (!user.getPin().equals(request.getPin()))
            // {
            //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
            //              body(new JbackCommonResponse().
            //                  setStatus("FAILURE").setMessage("Invalid PIN"));
            // }

            Type itemListType = new TypeToken<List<Item>>() {}.getType();
            List<Item> currentItems = JsonUtil.convertFromJson(user.getData(), itemListType);

            Set<String> currentItemIds = currentItems.stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            Set<String> itemsDeleteIds = request.getItemsDelete().stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            if (!currentItemIds.containsAll(itemsDeleteIds))
            {
                JbackCommonResponse response = new JbackCommonResponse()
                        .setStatus("FAILURE")
                        .setMessage("Some items to delete are not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            currentItems.removeIf(item -> itemsDeleteIds.contains(item.getItemId()));
            currentItems.addAll(request.getItemsAdd());

            JbackCommonResponse response = new JbackCommonResponse()
                    .setStatus("SUCCESS")
                    .setMessage("Items managed successfully");

            final String unlockResults = jbackDataService.saveUnlocked(user.getUserId(), request.getItemsUnlock());
            if (!unlockResults.isEmpty())
            {
                // ToDo: set message
                response.setMessage(unlockResults);
            }

            user.setData(JsonUtil.convertToJsonItems(currentItems));
            JBackDataRepository.save(user);

            response.setUserId(user.getUserId());
            response.setUserName(user.getUserName());
            response.setItems(currentItems);
            return ResponseEntity.ok(response);
        }

        JbackCommonResponse response = new JbackCommonResponse()
                .setStatus("FAILURE")
                .setMessage("User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/unlock")
    public ResponseEntity<Map<String, Object>> unlockItem(@RequestBody UnlockRequest request)
    {
        Map<String, Object> response = new HashMap<>();
        final String userId = request.getUserId();
        final Item itemUnlock = request.getItemUnlock();

        if (userId == null || itemUnlock == null || itemUnlock.getItemId() == null) {
            response.put("ERROR_MESSAGE", "userId or itemUnlock is missing");
            return ResponseEntity.badRequest().body(response);
        }

        List<Item> itemsToDelete = request.getItemsDelete();
        if (itemsToDelete != null && !itemsToDelete.isEmpty())
        {
            for (Item item : itemsToDelete)
            {
                Optional<UnlockedEntity> unlockedEntityOptional = jbackUnlockedRepository.
                        findByUserIdAndItemId(userId, item.getItemId());
                if (unlockedEntityOptional.isPresent())
                {
                    jbackUnlockedRepository.delete(unlockedEntityOptional.get());
                }
            }
        }

        // dataService.deleteAllUnlockedByUserId(userId);

        UnlockedEntity unlockedEntity = new UnlockedEntity();
        unlockedEntity.setUserId(userId);
        unlockedEntity.setItemId(itemUnlock.getItemId());
        unlockedEntity.setCount(1);
        jbackUnlockedRepository.save(unlockedEntity);
        response.put("message", "Item unlocked successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/lock")
    public ResponseEntity<Map<String, Object>> lockItem(@RequestHeader("Authorization") String token,
                                                        @RequestBody Map<String, String> request)
    {
        Map<String, Object> response = new HashMap<>();
        String userId = request.get("userId");
        String itemId = request.get("itemId");

        if (!isTokenValid(token, userId)) {
            response.put("ERROR_MESSAGE", "Invalid or missing token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (userId == null || itemId == null)
        {
            response.put("ERROR_MESSAGE", "userId or itemId is missing");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<UnlockedEntity> unlockedEntityOptional = jbackUnlockedRepository.findByUserIdAndItemId(userId, itemId);
        if (unlockedEntityOptional.isPresent())
        {
            jbackUnlockedRepository.delete(unlockedEntityOptional.get());
            response.put("message", "Item locked successfully");
        } else {
            response.put("ERROR_MESSAGE", "Unlocked item not found");
        }
        return ResponseEntity.ok(response);
    }

    private boolean isTokenValid(String token, String userId)
    {
        if (token == null || !token.startsWith("Bearer "))
        {
            return false;
        }
        token = token.substring(7);
        return jwtUtil.validateToken(token, userId);
    }

}