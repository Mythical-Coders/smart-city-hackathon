package com.authentification.springboot.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentification.springboot.models.ERole;
import com.authentification.springboot.models.Role;
import com.authentification.springboot.models.User;
import com.authentification.springboot.payload.request.LoginRequest;
import com.authentification.springboot.payload.request.SignupRequest;
import com.authentification.springboot.payload.response.JwtResponse;
import com.authentification.springboot.payload.response.MessageResponse;
import com.authentification.springboot.repository.RoleRepository;
import com.authentification.springboot.repository.UserRepository;
import com.authentification.springboot.security.jwt.JwtUtils; 
import com.authentification.springboot.security.services.UserDetailsImpl;

//This is a comment 
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role citizenRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(citizenRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "receptive":
					Role instructorRole = roleRepository.findByName(ERole.ROLE_RECEPTIVE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(instructorRole);

					break;
				case "driver":
					Role driverRole = roleRepository.findByName(ERole.ROLE_DRIVER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(driverRole);

					break;
				default:
					Role citizenRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(citizenRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@GetMapping("/all")
	public List<User> GetUsers() {
		return userRepository.findAll();
	}

	@PutMapping("/")
	public ResponseEntity<?> updateUser(@Valid @RequestBody SignupRequest signUpRequest) {

		// Create new user's account
		User oldUser = userRepository.findById(signUpRequest.getId()).orElse(null);

		if (!userRepository.existsByUsername(signUpRequest.getUsername())
				|| signUpRequest.getUsername().equals(oldUser.getUsername())) {
			oldUser.setUsername(signUpRequest.getUsername());
		} else
			return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));

		if (!userRepository.existsByEmail(signUpRequest.getEmail())
				|| signUpRequest.getEmail().equals(oldUser.getEmail())) {
			oldUser.setEmail(signUpRequest.getEmail());
		} else
			return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role citizenRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(citizenRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "receptive":
					Role instructorRole = roleRepository.findByName(ERole.ROLE_RECEPTIVE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(instructorRole);

					break;
				case "driver":
					Role driverRole = roleRepository.findByName(ERole.ROLE_DRIVER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(driverRole);

					break;
				default:
					Role citizenRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(citizenRole);
				}
			});
		}

		oldUser.setRoles(roles);
		userRepository.save(oldUser);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable String id) {
		userRepository.deleteById(id);
		return id;
	}

}
