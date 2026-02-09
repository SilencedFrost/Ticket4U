package com.ticket4u.feature.eventDetail.dto;

import com.ticket4u.core.Seat;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EventDetailResponse {
    private UUID eventId;
    private String eventTitle;

    private String date;
    private String time;

    private String address;
    private String description;

    private String minPrice;
    private String maxPrice;

    private ImageEventDTO imgEvent;
    private OrganizerDTO organizer;
    private List<ShowtimeDTO> showtimes;

    @Data
    public static class ImageEventDTO{
        private String heroUrl;
        private String seatMapUrl;
    }

    @Data
    public static class OrganizerDTO{
        private UUID id;
        private String name;
        private String avatar;
        private String description;
    }

    @Data
    public static class ShowtimeDTO {
        private String id;
        private String date;
        private String time;
        private List<SeatTypeDTO> seatTypes;
    }

    @Data
    public static class SeatTypeDTO {
        private UUID id;
        private String name;
        private String price;
        private Integer available;
        private String description;
        private String image;
        private List<String> benefits;
    }
}
