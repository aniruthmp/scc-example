import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should send a person message"
    label "triggerPerson"
    input {
        triggeredBy( "triggerMessage()")
    }
    outputMessage {
        sentTo "persons"
        body(["personId": 999, "firstName": "From", "lastName": "Producer"])
        headers {
            header("contentType", applicationJsonUtf8())
        }
    }
}