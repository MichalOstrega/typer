package pl.most.typer.service.footballservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.most.typer.configuration.ExternalFootballApiProps;

import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class FootballApiServiceDefault implements FootballApiService {


    private RestTemplate restTemplate;
    private ExternalFootballApiProps externalFootballApiProps;

    public FootballApiServiceDefault(RestTemplate restTemplate, ExternalFootballApiProps externalFootballApiProps) {
        this.restTemplate = restTemplate;
        this.externalFootballApiProps = externalFootballApiProps;
    }

    public <T> ResponseEntity<T> getExternalData(List<String> endpoints, Map<String,String> filters, Class<T> returnedEntity) {

        ResponseEntity<T> entity = restTemplate.exchange(
                prepareURL(endpoints,filters),
                HttpMethod.GET,
                getStringHttpEntity(),
                returnedEntity
        );
        return entity;
    }

    private String prepareURL(List<String> endpoints, Map<String,String> filters){
        StringBuilder stringBuilder = new StringBuilder().append(externalFootballApiProps.getBasicUrl());
        for (String endpoint : endpoints) {
            stringBuilder.append("/").append(endpoint);
        }
        stringBuilder.append("?");
        for (Map.Entry<String, String> entrySet : filters.entrySet()) {
            stringBuilder.append(entrySet.getKey()).append("=").append(entrySet.getValue()).append("&");
        }
        return stringBuilder.toString();
    }
    private HttpEntity<String> getStringHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(externalFootballApiProps.getTypeOfToken(), externalFootballApiProps.getApiToken());
        return new HttpEntity<>(httpHeaders);
    }
}
