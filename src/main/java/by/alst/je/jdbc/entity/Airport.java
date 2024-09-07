package by.alst.je.jdbc.entity;

import java.util.Objects;

public class Airport {

    private AirportCode code;
    private Country country;
    private City city;

    public Airport() {
    }

    public Airport(AirportCode code, Country country, City city) {
        this.code = code;
        this.country = country;
        this.city = city;
    }

    public AirportCode getCode() {
        return code;
    }

    public void setCode(AirportCode code) {
        this.code = code;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return code == airport.code && country == airport.country && city == airport.city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, country, city);
    }

    @Override
    public String toString() {
        return "Airport{" +
               "code=" + code +
               ", country=" + country +
               ", city=" + city +
               '}';
    }
}
