console.clear();
var result = "";
var limit = document.querySelector('select[name="proxylisttable_length"]');
limit.selectedIndex = 2;

var tr = document.querySelectorAll('[role="row"]');
var ip;
var port;
for (i = 0; i < tr.length; i++) {
  var row = tr.item(i);
  var td = row.querySelectorAll("td");
  ip = td[0];
  port = td[1];
  if (typeof ip != "undefined" && typeof port != "undefined") {
    var proxy = `${ip.innerText}:${port.innerText}\n`;
    result += proxy;
  }
}

//console.log(result)
return result