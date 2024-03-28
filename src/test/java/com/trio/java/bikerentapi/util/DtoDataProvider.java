package com.trio.java.bikerentapi.util;

import com.trio.java.bikerentapi.dto.RentDto;

public final class DtoDataProvider {

    private DtoDataProvider() {
    }

    public static RentDto getRentDto() {
        return ObjectMapperHelper.getInstance().converFileToObject(
                "rent/rentDto.json", RentDto.class);
    }
}
