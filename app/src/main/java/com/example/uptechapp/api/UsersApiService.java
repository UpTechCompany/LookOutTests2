package com.example.uptechapp.api;

import com.example.uptechapp.dao.RetrofitService;

public class UsersApiService {

    private static UsersApi usersApi;

    private static UsersApi create() {
        return RetrofitService.getInstance().create(UsersApi.class);
    }

    public static UsersApi getInstance() {
        if (usersApi == null) usersApi = create();
        return usersApi;
    }
}
