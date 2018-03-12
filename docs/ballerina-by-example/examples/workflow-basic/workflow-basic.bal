workflow OrderProcess(json orderData) {
        io:println("Processing the order..");
        io:println(orderData.customer);
}
