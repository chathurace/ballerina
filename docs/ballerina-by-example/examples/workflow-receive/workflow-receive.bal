import ballerina.io;

workflow OrderProcess(json orderData) {
        string orderId = orderData.orderId.toString();
        string customer = orderData.customer.toString();
        io:println(orderId + ": Processing started for customer " + customer);

        io:println(orderId + ": Waiting for order approval...");
        json correlation1 = {applicationId:orderId};
        json approval = receive("internalApproval", correlation1);
        string status = approval.status.toString();

        if (status == "approved") {
            io:println(orderId + ": Order is approved");

            io:println(orderId + ": Waiting for customer credit check...");
            json correlation2 = {customerId:customer};
            json credit = receive("customerCredit", correlation2);
            io:print(orderId + ": Customer credit amount: ");
            io:println(credit.amount);
        } else {
            io:println(orderId + ": Order is not approved");
        }
        io:println(orderId + ": Processing order completed");
}