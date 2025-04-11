package com.goga74.platform.controller;

import com.goga74.platform.DB.entity.UserEntity;
import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.DB.repository.DataRepository;
import com.goga74.platform.controller.dto.*;
import com.goga74.platform.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
public class JsonController
{
    private final DataRepository dataRepository;

    public JsonController(DataRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody CreateRequest request)
    {
        Optional<UserEntity> existingUser = dataRepository.findById(request.getUserId());
        if (existingUser.isEmpty())
        {
            UserEntity user = new UserEntity();
            user.setUserId(request.getUserId());
            user.setUserName(request.getUserName());
            user.setData(JsonUtil.convertToJson(request.getItems()));
            dataRepository.save(user);
            return ResponseEntity.ok("User created successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    @PostMapping("/login")
    public CommonResponse loginUser(@RequestBody LoginRequest request)
    {
        if (request != null)
        {
            final String userId = request.getUserId();
            Optional<UserEntity> userOptional = dataRepository.findById(userId);
            if (userOptional.isEmpty())
            {
                return new CommonResponse()
                        .setStatus("error")
                        .setMessage("User not found")
                        .setUserId(request.getUserId());
            }

            UserEntity user = userOptional.get();
            // Using TypeToken to specify the exact type
            Type itemListType = new TypeToken<List<ItemEntity>>() {}.getType();
            List<Item> items = JsonUtil.convertFromJson(user.getData(), itemListType);

            return new CommonResponse()
                    .setStatus("success")
                    .setUserId(userId)
                    .setUserName(user.getUserName())
                    .setItems(items)
                    .setMessage("User data retrieved successfully");
        }
        return new CommonResponse()
            .setStatus("error")
            .setMessage("userId is null");
    }

    @Operation(summary = "Get user data by user ID",
            description = "Retrieve detailed user information along with associated items by user ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of user data",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CommonResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/user/{userId}")
    public CommonResponse getUserById(@PathVariable String userId)
    {
        Optional<UserEntity> userOptional = dataRepository.findById(userId);
        if (userOptional.isEmpty())
        {
            return new CommonResponse()
                    .setStatus("error")
                    .setMessage("User not found")
                    .setUserId(userId);
        }
        UserEntity user = userOptional.get();
        // Using TypeToken to specify the exact type
        Type itemListType = new TypeToken<List<Item>>() {}.getType();
        List<Item> items = JsonUtil.convertFromJson(user.getData(), itemListType);
        return new CommonResponse()
                .setStatus("success")
                .setUserId(userId)
                .setUserName(user.getUserName())
                .setItems(items)
                .setMessage("User data retrieved successfully");
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
