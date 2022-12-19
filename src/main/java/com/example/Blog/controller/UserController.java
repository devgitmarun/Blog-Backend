package com.example.Blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Blog.model.User;
import com.example.Blog.playloads.ApiResponse;
import com.example.Blog.playloads.LoginDto;
import com.example.Blog.playloads.UserDto;
import com.example.Blog.repository.UserRepository;
import com.example.Blog.service.UserService;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository repository;
	
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createUserDto=this.userService.createUser(userDto);
		return new ResponseEntity<>(createUserDto,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserbyId(@PathVariable Long userId){
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable("userId") Long userId){
		 UserDto updatUserDto=this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updatUserDto);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUserbyId(@PathVariable Long userId){
		
		 this.userService.deleteUser(userId); 
		 return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted successfully",true),HttpStatus.OK);
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<Map<String,String>> UserLogin(@RequestBody LoginDto login){
		
		Optional<User> userDetail=this.repository.findByemail(login.getEmail());
		Map<String,String> response=new HashMap<String,String>();
		
		if(userDetail.isPresent())
		{
		  if(userDetail.get().getPassword().equals(login.getPassword()))	
		  {
			  response.put("status", "true");
			  response.put("message", "login successful");
			  return new ResponseEntity<Map<String,String>> (response,HttpStatus.CREATED);
		  }
		  else
		  {
			  response.put("status", "false");
			  response.put("message", "login unsuccessful");
			  return new ResponseEntity<Map<String,String>> (response,HttpStatus.CREATED);
		  }
		}
		else
		{
			 response.put("status", "false");
			 response.put("message", "login failed");
			 return new ResponseEntity<Map<String,String>> (response,HttpStatus.CREATED);
		}
	}
}
