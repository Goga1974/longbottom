package com.goga74.platform.controller;

import com.goga74.platform.DB.entity.RequestLog;
import com.goga74.platform.DB.entity.UnlockedEntity;
import com.goga74.platform.DB.entity.UserEntity;
import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.DB.repository.DataRepository;
import com.goga74.platform.DB.repository.ItemRepository;
import com.goga74.platform.DB.repository.RequestLogRepository;
import com.goga74.platform.DB.repository.UnlockedRepository;
import com.goga74.platform.DB.service.DataService;
import com.goga74.platform.controller.dto.*;
import com.goga74.platform.controller.dto.request.CreateRequest;
import com.goga74.platform.controller.dto.request.LoginRequest;
import com.goga74.platform.controller.dto.request.TransactionRequest;
import com.goga74.platform.controller.dto.request.UnlockRequest;
import com.goga74.platform.controller.dto.response.JbackCommonResponse;
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

    private final DataService dataService;
    private final DataRepository dataRepository;
    private final ItemRepository itemRepository;
    private final RequestLogRepository requestLogRepository;
    private final UnlockedRepository unlockedRepository;
    private final JWTUtil jwtUtil; // Добавляем JwtUtil

    @Value("${request.limit.minutes}")
    private int requestLimitMinutes;

    public JbackController(DataService dataService,
                           DataRepository dataRepository,
                           ItemRepository itemRepository,
                           RequestLogRepository requestLogRepository,
                           UnlockedRepository unlockedRepository,
                           JWTUtil jwtUtil) { // Добавляем в конструктор
        this.dataService = dataService;
        this.dataRepository = dataRepository;
        this.itemRepository = itemRepository;
        this.requestLogRepository = requestLogRepository;
        this.unlockedRepository = unlockedRepository;
        this.jwtUtil = jwtUtil; // Инициализируем jwtUtil
    }

    /*
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateRequest request, HttpServletRequest httpRequest) {
        Map<String, Object> response = new HashMap<>();

        // Получение IP и User-Agent из HttpServletRequest
        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        // Запись в таблицу request_log
        RequestLog requestLog = new RequestLog();
        requestLog.setUserId(request.getUserId());
        requestLog.setIpAddress(ipAddress);
        requestLog.setUserAgent(userAgent);

        // Получение текущего времени в GMT
        ZonedDateTime gmtTime = ZonedDateTime.now(ZoneId.of("GMT"));
        LocalDateTime gmtLocalDateTime = gmtTime.toLocalDateTime();
        requestLog.setRequestTime(gmtLocalDateTime);
        requestLogRepository.save(requestLog);

        // Остальная логика создания пользователя
        try {
            Optional<UserEntity> existingUser = dataRepository.findById(request.getUserId());
            if (existingUser.isEmpty()) {
                UserEntity user = new UserEntity();
                user.setUserId(request.getUserId());
                user.setUserName(request.getUserName());
                dataRepository.save(user);

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
                itemRepository.saveAll(items);

                // Сохранение в таблицу unlocked
                if (!request.getUnlocked().isEmpty()) {
                    UnlockedEntity unlockedEntity = new UnlockedEntity();
                    unlockedEntity.setItemId(request.getUnlocked().get(0).getItemId());
                    unlockedEntity.setUserId(request.getUserId());
                    unlockedEntity.setCount(1);
                    unlockedRepository.save(unlockedEntity);
                }

                response.put("message", "User and items created successfully");
            } else {
                response.put("ERROR_MESSAGE", "User already exists");
            }
        } catch (Exception e) {
            response.put("ERROR_MESSAGE", "An error occurred: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    */

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateRequest request, HttpServletRequest httpRequest)
    {
        Map<String, Object> response = new HashMap<>();

        // Получение IP и User-Agent из HttpServletRequest
        final String ipAddress = httpRequest.getRemoteAddr();

        // Проверка последнего запроса с этого IP
        Optional<RequestLog> lastRequestOpt = requestLogRepository.
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

        final String userAgent = httpRequest.getHeader("User-Agent");
        // Остальная логика создания пользователя
        try
        {
            Optional<UserEntity> existingUser = dataRepository.findById(request.getUserId());
            if (existingUser.isEmpty()) {
                UserEntity user = new UserEntity();
                user.setUserId(request.getUserId());
                user.setUserName(request.getUserName());
                dataRepository.save(user);

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
                itemRepository.saveAll(items);

                // Сохранение в таблицу unlocked
                if (!request.getUnlocked().isEmpty())
                {
                    UnlockedEntity unlockedEntity = new UnlockedEntity();
                    unlockedEntity.setItemId(request.getUnlocked().get(0).getItemId());
                    unlockedEntity.setUserId(request.getUserId());
                    unlockedEntity.setCount(1);
                    unlockedRepository.save(unlockedEntity);
                }

                dataService.logRequest(request.getUserId(), ipAddress, userAgent);
                response.put("message", "User and items created successfully");
            } else {
                response.put("ERROR_MESSAGE", "User already exists");
            }
        } catch (Exception e) {
            response.put("ERROR_MESSAGE", "An error occurred: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        if (request != null && request.getUserId() != null) {
            String userId = request.getUserId();
            Map<String, Object> userData = dataService.getUser(userId);

            if (userData.containsKey("ERROR_MESSAGE")) {
                response.put("ERROR_MESSAGE", userData.get("ERROR_MESSAGE"));
                return ResponseEntity.ok(response);
            }

            // Генерация JWT токена и добавление его в ответ
            final String token = jwtUtil.generateToken(userId);
            if (dataService.saveToken(userId, token) != null)
            {
                response.put("token", token);
            }

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
            Map<String, Object> userData = dataService.getUser(userId);

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
    public ResponseEntity<JbackCommonResponse> handleTransaction(@RequestBody TransactionRequest request) {
        Optional<UserEntity> userOptional = dataRepository.findById(request.getUserId());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            Type itemListType = new TypeToken<List<Item>>() {}.getType();
            List<Item> currentItems = JsonUtil.convertFromJson(user.getData(), itemListType);

            Set<String> currentItemIds = currentItems.stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            Set<String> itemsDeleteIds = request.getItemsDelete().stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            if (!currentItemIds.containsAll(itemsDeleteIds)) {
                JbackCommonResponse response = new JbackCommonResponse()
                        .setStatus("FAILURE")
                        .setMessage("Some items to delete are not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            currentItems.removeIf(item -> itemsDeleteIds.contains(item.getItemId()));
            currentItems.addAll(request.getItemsAdd());

            user.setData(JsonUtil.convertToJsonItems(currentItems));
            dataRepository.save(user);

            JbackCommonResponse response = new JbackCommonResponse()
                    .setStatus("SUCCESS")
                    .setMessage("Items managed successfully");
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
    public ResponseEntity<Map<String, Object>> unlockItem(@RequestBody UnlockRequest request) {
        Map<String, Object> response = new HashMap<>();
        final String userId = request.getUserId();
        final Item itemUnlock = request.getItemUnlock();

        if (userId == null || itemUnlock == null || itemUnlock.getItemId() == null) {
            response.put("ERROR_MESSAGE", "userId or itemUnlock is missing");
            return ResponseEntity.badRequest().body(response);
        }

        List<Item> itemsToDelete = request.getItemsDelete();
        if (itemsToDelete != null && !itemsToDelete.isEmpty()) {
            for (Item item : itemsToDelete) {
                Optional<UnlockedEntity> unlockedEntityOptional = unlockedRepository.findByUserIdAndItemId(userId, item.getItemId());
                if (unlockedEntityOptional.isPresent()) {
                    unlockedRepository.delete(unlockedEntityOptional.get());
                }
            }
        }

        // dataService.deleteAllUnlockedByUserId(userId);

        UnlockedEntity unlockedEntity = new UnlockedEntity();
        unlockedEntity.setUserId(userId);
        unlockedEntity.setItemId(itemUnlock.getItemId());
        unlockedEntity.setCount(1);
        unlockedRepository.save(unlockedEntity);
        response.put("message", "Item unlocked successfully");

        return ResponseEntity.ok(response);
    }

    /*
    @PostMapping("/unlock")
    public ResponseEntity<Map<String, Object>> unlockItem(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        final String userId = request.get("userId");
        final String itemId = request.get("itemId");

        if (userId == null || itemId == null) {
            response.put("ERROR_MESSAGE", "userId or itemId is missing");
            return ResponseEntity.badRequest().body(response);
        }

        dataService.deleteAllUnlockedByUserId(userId);

        UnlockedEntity unlockedEntity = new UnlockedEntity();
        unlockedEntity.setUserId(userId);
        unlockedEntity.setItemId(itemId);
        unlockedEntity.setCount(1);
        unlockedRepository.save(unlockedEntity);
        response.put("message", "Item unlocked successfully");

        return ResponseEntity.ok(response);
    }
    */

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

        if (userId == null || itemId == null) {
            response.put("ERROR_MESSAGE", "userId or itemId is missing");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<UnlockedEntity> unlockedEntityOptional = unlockedRepository.findByUserIdAndItemId(userId, itemId);
        if (unlockedEntityOptional.isPresent()) {
            unlockedRepository.delete(unlockedEntityOptional.get());
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