package com.github.zemke.tippspiel2.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.zemke.tippspiel2.core.profile.Prod
import com.github.zemke.tippspiel2.core.properties.FootballDataProperties
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.hal.Jackson2HalModule
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import javax.annotation.PostConstruct


@Prod
@Service
open class FootballDataServiceImpl : FootballDataService {

    @Autowired private lateinit var footballDataProperties: FootballDataProperties
    private lateinit var restTemplate: RestTemplate


    @PostConstruct
    fun postConstruct() {
        restTemplate = RestTemplateBuilder()
                .interceptors(
                        listOf(HttpHeaderInterceptor(footballDataProperties.apiTokenHeader, footballDataProperties.apiToken)))
                .messageConverters(
                        with(MappingJackson2HttpMessageConverter()) {
                            supportedMediaTypes = listOf(MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON)
                            objectMapper = with(ObjectMapper()) {
                                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                registerModule(Jackson2HalModule())
                            }
                            this
                        })
                .build()
    }

    override fun requestCompetition(competitionId: Long): FootballDataCompetitionDto {
        return restTemplate.getForObject(
                "${footballDataProperties.endpoint}/competitions/$competitionId", FootballDataCompetitionDto::class.java)
    }
}