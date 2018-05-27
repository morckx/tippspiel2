package com.github.zemke.tippspiel2.persistence.model.embeddable

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
data class FullName(

        @NotBlank var firstName: String,
        @NotBlank var lastName: String
)
