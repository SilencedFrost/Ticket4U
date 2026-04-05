rootProject.name = "ticket-4u-services-reactive"

include(":services:payment-service")
include(":services:ticket-service")

project(":services:payment-service").projectDir = file("services/payment-service")
project(":services:ticket-service").projectDir = file("services/ticket-service")