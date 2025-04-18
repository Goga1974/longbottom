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
import com.goga74.platform.util.JsonUtil;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class JsonController
{
    private final DataService dataService;
    private final DataRepository dataRepository;
    private final ItemRepository itemRepository;
    private final RequestLogRepository requestLogRepository;
    private final UnlockedRepository unlockedRepository;

    //@Autowired
    public JsonController(DataService dataService,
                          DataRepository dataRepository,
                          ItemRepository itemRepository,
                          RequestLogRepository requestLogRepository,
                          UnlockedRepository unlockedRepository)
    {
        this.dataService = dataService;
        this.dataRepository = dataRepository;
        this.itemRepository = itemRepository;
        this.requestLogRepository = requestLogRepository;
        this.unlockedRepository = unlockedRepository;
    }

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
                if (!request.getUnlocked().isEmpty())
                {
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest request)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success"); // По умолчанию статус успешный

        if (request != null && request.getUserId() != null)
        {
            String userId = request.getUserId();
            Map<String, Object> userData = dataService.getUser(userId);

            if (userData.containsKey("ERROR_MESSAGE"))
            {
                response.put("ERROR_MESSAGE", userData.get("ERROR_MESSAGE"));
                return ResponseEntity.ok(response); // Возвращаем статус 200 с сообщением об ошибке
            }

            response.putAll(userData); // Добавляем данные пользователя в ответ
            return ResponseEntity.ok(response);
        }

        response.put("ERROR_MESSAGE", "userId is null"); // Если userId равен null, тоже добавляем сообщение
        return ResponseEntity.ok(response); // Возвращаем статус 200 с сообщением об ошибке
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success"); // По умолчанию статус успешный

        if (userId != null) {
            Map<String, Object> userData = dataService.getUser(userId);

            if (userData.containsKey("ERROR_MESSAGE")) {
                response.put("ERROR_MESSAGE", userData.get("ERROR_MESSAGE"));
                return ResponseEntity.ok(response); // Возвращаем статус 200 с сообщением об ошибке
            }

            response.putAll(userData); // Добавляем данные пользователя в ответ
            return ResponseEntity.ok(response);
        }

        response.put("ERROR_MESSAGE", "userId is null"); // Если userId равен null, тоже добавляем сообщение
        return ResponseEntity.ok(response); // Возвращаем статус 200 с сообщением об ошибке
    }

    @Operation(summary = "Update user data", description = "Update the data of a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of user data"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/update")
    public ResponseEntity<String> updateUserData(@RequestBody UpdateRequest request)
    {
        // Find the user by ID
        Optional<UserEntity> userOptional = dataRepository.findById(request.getUserId());
        if (userOptional.isPresent())
        {
            UserEntity user = userOptional.get();
            user.setData(request.getData());
            dataRepository.save(user);
            return ResponseEntity.ok("User data updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PostMapping("/transaction")
    public ResponseEntity<CommonResponse> handleTransaction(@RequestBody TransactionRequest request)
    {
        Optional<UserEntity> userOptional = dataRepository.findById(request.getUserId());
        if (userOptional.isPresent())
        {
            UserEntity user = userOptional.get();
            // Deserialize the current items
            //Type itemListType = new TypeToken<List<ItemEntity>>() {}.getType();
            Type itemListType = new TypeToken<List<Item>>() {}.getType();
            List<Item> currentItems = JsonUtil.convertFromJson(user.getData(), itemListType);

            // Extract item IDs from currentItems
            Set<String> currentItemIds = currentItems.stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            // Extract item IDs from itemsDelete
            Set<String> itemsDeleteIds = request.getItemsDelete().stream()
                    .map(Item::getItemId)
                    .collect(Collectors.toSet());

            // Check if all itemsDelete are present in currentItems
            if (!currentItemIds.containsAll(itemsDeleteIds))
            {
                // Return error if some items to delete are not present
                CommonResponse response = new CommonResponse()
                        .setStatus("FAILURE")
                        .setMessage("Some items to delete are not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Remove items to delete
            currentItems.removeIf(item -> itemsDeleteIds.contains(item.getItemId()));

            // Add new items
            currentItems.addAll(request.getItemsAdd());

            user.setData(JsonUtil.convertToJson(currentItems));
            dataRepository.save(user);

            CommonResponse response = new CommonResponse()
                    .setStatus("SUCCESS")
                    .setMessage("Items managed successfully");
            response.setUserId(user.getUserId());
            response.setUserName(user.getUserName());
            response.setItems(currentItems);
            return ResponseEntity.ok(response);
        }
        CommonResponse response = new CommonResponse()
                .setStatus("FAILURE")
                .setMessage("User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
