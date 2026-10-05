package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.screen.AddScreenRequestDTO;
import com.microservices.gateway.DTOS.screen.ScreenResponseDTO;
import com.microservices.gateway.DTOS.screen.SeatLayoutRequestDTO;
import com.microservices.gateway.DTOS.screen.SeatLayoutResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterListResponseDTO;
import com.microservices.gateway.DTOS.theater.TheaterRequestDTO;
import com.microservices.gateway.DTOS.theater.TheaterResponseDTO;
import com.microservices.movie.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TheaterGRPCService {

    @GrpcClient("theater-service")
    private TheaterServiceGrpc.TheaterServiceBlockingStub theaterServiceBlockingStub;

    public TheaterResponseDTO CreateTheater(TheaterRequestDTO theaterRequestDTO) {

        CreateTheaterRequest createTheaterRequest = CreateTheaterRequest.newBuilder()
                .setName(theaterRequestDTO.name())
                .setAddress(theaterRequestDTO.address())
                .setCity(theaterRequestDTO.city())
                .setState(theaterRequestDTO.state())
                .setPincode(theaterRequestDTO.pincode())
                .setLatitude(theaterRequestDTO.latitude())
                .setLongitude(theaterRequestDTO.longitude())
                .setPhone(theaterRequestDTO.phone())
                .setEmail(theaterRequestDTO.email())
                .addAllAmenities(theaterRequestDTO.amenities())
                .build();

        TheaterResponse theaterResponse = theaterServiceBlockingStub.createTheater(createTheaterRequest);
        TheaterResponseDTO theaterResponseDTO = new TheaterResponseDTO(
                theaterResponse.getTheaterId(),
                theaterResponse.getName(),
                theaterResponse.getAddress(),
                theaterResponse.getCity(),
                theaterResponse.getState(),
                theaterResponse.getPincode(),
                theaterResponse.getLatitude(),
                theaterResponse.getLongitude(),
                theaterResponse.getPhone(),
                theaterResponse.getEmail(),
                theaterResponse.getAmenitiesList(),

                Instant.ofEpochSecond(theaterResponse.getCreatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate(),

                Instant.ofEpochSecond(theaterResponse.getUpdatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate()
        );
        return theaterResponseDTO;

    }

    public TheaterListResponseDTO getAllTheaters(int page, int size, String city) {

        ListTheatersRequest listTheatersRequest = ListTheatersRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        ListTheatersResponse theaterResponses = theaterServiceBlockingStub.listTheaters(listTheatersRequest);

        TheaterListResponseDTO theaterListResponseDTO = new TheaterListResponseDTO(
                mapTheaterResponsesToDTOs(theaterResponses),
                theaterResponses.getTotalCount(),
                theaterResponses.getPage(),
                theaterResponses.getSize(),
                theaterResponses.getTotalPages(),
                theaterResponses.getHasNext()
        );
        return theaterListResponseDTO;
    }

    public TheaterListResponseDTO searchTheaters(String query, int limit) {
        SearchTheatersRequest searchTheatersRequest = SearchTheatersRequest.newBuilder()
                .setQuery(query == null ? "" : query)
                .setLimit(limit)
                .build();

        ListTheatersResponse theaterResponses = theaterServiceBlockingStub.searchTheaters(searchTheatersRequest);

        return new TheaterListResponseDTO(
                mapTheaterResponsesToDTOs(theaterResponses),
                theaterResponses.getTotalCount(),
                theaterResponses.getPage(),
                theaterResponses.getSize(),
                theaterResponses.getTotalPages(),
                theaterResponses.getHasNext()
        );
    }


    private List<TheaterResponseDTO> mapTheaterResponsesToDTOs(ListTheatersResponse theaterResponses) {
        return theaterResponses.getTheatersList().stream().map(theaterResponse -> new TheaterResponseDTO(
                theaterResponse.getTheaterId(),
                theaterResponse.getName(),
                theaterResponse.getAddress(),
                theaterResponse.getCity(),
                theaterResponse.getState(),
                theaterResponse.getPincode(),
                theaterResponse.getLatitude(),
                theaterResponse.getLongitude(),
                theaterResponse.getPhone(),
                theaterResponse.getEmail(),
                theaterResponse.getAmenitiesList(),

                Instant.ofEpochSecond(theaterResponse.getCreatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate(),

                Instant.ofEpochSecond(theaterResponse.getUpdatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate()
        )).toList();
    }


    public TheaterResponseDTO getTheaterById(String theaterId) {
        GetTheaterRequest getTheaterRequest = GetTheaterRequest.newBuilder()
                .setTheaterId(theaterId)
                .build();

        TheaterResponse response = theaterServiceBlockingStub.getTheater(getTheaterRequest);

        TheaterResponseDTO theaterResponseDTO = new TheaterResponseDTO(
                response.getTheaterId(),
                response.getName(),
                response.getAddress(),
                response.getCity(),
                response.getState(),
                response.getPincode(),
                response.getLatitude(),
                response.getLongitude(),
                response.getPhone(),
                response.getEmail(),
                response.getAmenitiesList(),

                Instant.ofEpochSecond(response.getCreatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate(),

                Instant.ofEpochSecond(response.getUpdatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate()
        );
        return theaterResponseDTO;
    }

    public TheaterResponseDTO updateTheater(String theaterId, TheaterRequestDTO theaterRequestDTO) {

        UpdateTheaterRequest updateTheaterRequest = UpdateTheaterRequest.newBuilder()
                .setTheaterId(theaterId)
                .setName(theaterRequestDTO.name())
                .setAddress(theaterRequestDTO.address())
                .setCity(theaterRequestDTO.city())
                .setState(theaterRequestDTO.state())
                .setPincode(theaterRequestDTO.pincode())
                .setLatitude(theaterRequestDTO.latitude())
                .setLongitude(theaterRequestDTO.longitude())
                .setPhone(theaterRequestDTO.phone())
                .setEmail(theaterRequestDTO.email())
                .addAllAmenities(theaterRequestDTO.amenities())
                .build();

        TheaterResponse response = theaterServiceBlockingStub.updateTheater(updateTheaterRequest);

        TheaterResponseDTO theaterResponseDTO = new TheaterResponseDTO(
                response.getTheaterId(),
                response.getName(),
                response.getAddress(),
                response.getCity(),
                response.getState(),
                response.getPincode(),
                response.getLatitude(),
                response.getLongitude(),
                response.getPhone(),
                response.getEmail(),
                response.getAmenitiesList(),

                Instant.ofEpochSecond(response.getCreatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate(),

                Instant.ofEpochSecond(response.getUpdatedAt().getSeconds())
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate()
        );
        return theaterResponseDTO;
    }

    public boolean deleteTheaterById(String theaterId) {
        DeleteTheaterRequest deleteTheaterRequest = DeleteTheaterRequest.newBuilder()
                .setTheaterId(theaterId)
                .build();
        DeleteTheaterResponse response = theaterServiceBlockingStub.deleteTheater(deleteTheaterRequest);
        return response.getSuccess();
    }

    // Screen Management Methods

    public ScreenResponseDTO addScreen(AddScreenRequestDTO addScreenRequestDTO) {
        log.info("Adding screen to theater: {}", addScreenRequestDTO.theaterId());
        
        List<SeatLayoutRequest> seatLayoutRequests = addScreenRequestDTO.seatLayout().stream()
                .map(seat -> SeatLayoutRequest.newBuilder()
                        .setRowName(seat.rowName())
                        .setStartSeatNumber(seat.startSeatNumber())
                        .setEndSeatNumber(seat.endSeatNumber())
                        .setSeatType(seat.seatType())
                        .setPriceMultiplier(seat.priceMultiplier())
                        .build())
                .collect(Collectors.toList());

        AddScreenRequest addScreenRequest = AddScreenRequest.newBuilder()
                .setTheaterId(addScreenRequestDTO.theaterId())
                .setScreenName(addScreenRequestDTO.screenName())
                .setScreenNumber(addScreenRequestDTO.screenNumber())
                .setTotalRows(addScreenRequestDTO.totalRows())
                .setTotalSeats(addScreenRequestDTO.totalSeats())
                .setScreenType(addScreenRequestDTO.screenType())
                .addAllSeatLayout(seatLayoutRequests)
                .build();

        ScreenResponse screenResponse = theaterServiceBlockingStub.addScreen(addScreenRequest);
        return mapScreenResponseToDTO(screenResponse);
    }

    public ScreenResponseDTO getScreen(String screenId) {
        log.info("Getting screen: {}", screenId);
        
        GetScreenRequest getScreenRequest = GetScreenRequest.newBuilder()
                .setScreenId(screenId)
                .build();

        ScreenResponse screenResponse = theaterServiceBlockingStub.getScreen(getScreenRequest);
        return mapScreenResponseToDTO(screenResponse);
    }

    public ScreenResponseDTO updateScreen(String theaterId, String screenId, AddScreenRequestDTO updateScreenRequestDTO) {
        log.info("Updating screen: {} for theater: {}", screenId, theaterId);

        List<SeatLayoutRequest> seatLayoutRequests = updateScreenRequestDTO.seatLayout().stream()
                .map(seat -> SeatLayoutRequest.newBuilder()
                        .setRowName(seat.rowName())
                        .setStartSeatNumber(seat.startSeatNumber())
                        .setEndSeatNumber(seat.endSeatNumber())
                        .setSeatType(seat.seatType())
                        .setPriceMultiplier(seat.priceMultiplier())
                        .build())
                .collect(Collectors.toList());

        UpdateScreenRequest updateScreenRequest = UpdateScreenRequest.newBuilder()
                .setTheaterId(theaterId)
                .setScreenId(screenId)
                .setScreenName(updateScreenRequestDTO.screenName())
                .setScreenNumber(updateScreenRequestDTO.screenNumber())
                .setTotalRows(updateScreenRequestDTO.totalRows())
                .setTotalSeats(updateScreenRequestDTO.totalSeats())
                .setScreenType(updateScreenRequestDTO.screenType())
                .addAllSeatLayout(seatLayoutRequests)
                .build();

        ScreenResponse screenResponse = theaterServiceBlockingStub.updateScreen(updateScreenRequest);
        return mapScreenResponseToDTO(screenResponse);
    }

    public List<ScreenResponseDTO> listScreensByTheater(String theaterId) {
        log.info("Listing screens for theater: {}", theaterId);
        
        ListScreensByTheaterRequest request = ListScreensByTheaterRequest.newBuilder()
                .setTheaterId(theaterId)
                .build();

        ListScreensResponse response = theaterServiceBlockingStub.listScreensByTheater(request);
        return response.getScreensList().stream()
                .map(this::mapScreenResponseToDTO)
                .collect(Collectors.toList());
    }

    public List<SeatLayoutResponseDTO> getSeatLayout(String screenId) {
        log.info("Getting seat layout for screen: {}", screenId);
        GetScreenRequest request = GetScreenRequest.newBuilder().setScreenId(screenId).build();
        ScreenResponse response = theaterServiceBlockingStub.getScreen(request);
        return response.getSeatLayoutList().stream()
                .map(seat -> new SeatLayoutResponseDTO(
                        seat.getRowName(),
                        seat.getStartSeatNumber(),
                        seat.getEndSeatNumber(),
                        seat.getSeatType(),
                        seat.getPriceMultiplier()
                ))
                .collect(Collectors.toList());
    }

    private ScreenResponseDTO mapScreenResponseToDTO(ScreenResponse screenResponse) {
        List<SeatLayoutResponseDTO> seatLayout = screenResponse.getSeatLayoutList().stream()
                .map(seat -> new SeatLayoutResponseDTO(
                        seat.getRowName(),
                        seat.getStartSeatNumber(),
                        seat.getEndSeatNumber(),
                        seat.getSeatType(),
                        seat.getPriceMultiplier()
                ))
                .collect(Collectors.toList());

        return new ScreenResponseDTO(
                screenResponse.getScreenId(),
                screenResponse.getTheaterId(),
                screenResponse.getScreenName(),
                screenResponse.getScreenNumber(),
                screenResponse.getTotalSeats(),
                screenResponse.getScreenType(),
                seatLayout,
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(screenResponse.getCreatedAt().getSeconds()),
                        ZoneOffset.UTC
                ),
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(screenResponse.getUpdatedAt().getSeconds()),
                        ZoneOffset.UTC
                )
        );
    }
}
