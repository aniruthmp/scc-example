import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should send a person message"
    label "triggerPerson"
    input {
        triggeredBy( "triggerMessage()")
    }
    outputMessage {
        sentTo "persons"
        body([firstName: "From", lastName: "Producer"])
    }
}