package com.example.routes

import com.example.model.Customer
import com.example.model.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * we're using the route function to group everything that falls under the /customer endpoint
 */
fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        //We can access their value using the indexed access operator (call.parameters["myParamName"])
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                //If it does not exist, we respond with a 400 Bad Request status code and an error message
                "Missing id",
                status = HttpStatusCode.BadRequest //400
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    //we'll return a 404 "Not Found" status code with an error message.
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound //404
                )
            //If we find it, we'll respond with the object.
            call.respond(customer)
        }
        post {
            //Calling it with the generic parameter Customer automatically deserializes the JSON request body into a Kotlin Customer object
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            //We'll respond with the newly created customer object and a 201 Created status code.
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created) //201
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)  //400
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted) //202
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound) //404
            }
        }
    }
}