
function receiveFunction () {
	json p = {fname:"John", lname:"Stallone", "age":10};

	string msg = "messageName";
    var res = receive(msg, p);
    return res;
}
