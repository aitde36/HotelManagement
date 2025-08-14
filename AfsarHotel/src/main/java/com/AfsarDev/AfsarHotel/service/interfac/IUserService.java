package com.AfsarDev.AfsarHotel.service.interfac;


import com.AfsarDev.AfsarHotel.dto.LoginRequest;
import com.AfsarDev.AfsarHotel.dto.Response;
import com.AfsarDev.AfsarHotel.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}
