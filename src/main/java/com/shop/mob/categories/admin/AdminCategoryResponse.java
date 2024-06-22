package com.shop.mob.categories.admin;

import java.util.List;

public class AdminCategoryResponse<T> {

    private boolean success;
    private String message;
    private List<T> data;

    public AdminCategoryResponse(List<T> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


}
