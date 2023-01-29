package az.et.mspayment.client;

import az.et.mspayment.model.client.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}")
@RequestMapping("api/countries")
public interface CountryClient {
    @GetMapping
    List<CountryDto> getAllAvailableCountries(@RequestParam String currency);

}
