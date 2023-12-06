package pl.ioad.entity;

public record UserCreate(
        String first_name,
        String last_name,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String email,
        String password
) {
}
