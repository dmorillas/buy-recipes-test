package co.morillas.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AddRecipeRequest {
    @NotBlank(message = "recipe name can't be null or empty")
    private String name;

    @NotEmpty(message = "recipe product ids can't be empty")
    private List<Integer> productIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}