import groovy.json.JsonSlurper
import org.springframework.cloud.contract.spec.Contract

def jsonSlurper = new JsonSlurper()
def respBody =  jsonSlurper.parseText('[{"firstName":"Aniruth","lastName":"Parthasarathy"},{"firstName":"Scott","lastName":"Tiger"}]')

Contract.make {
    description "should return a list of persons"


    request {
        url "/persons"
        method GET()
    }
    response {
        status 201
        body(respBody)
    }
}