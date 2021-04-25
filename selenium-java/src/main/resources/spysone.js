var proxies = document.getElementsByClassName("spy14");
var proxies_summary = "";
for (var i = 0; i < proxies.length; i++) {
   var inner_proxy = proxies.item(i).innerText;
   proxies_summary += inner_proxy + "\n";
   console.warn(inner_proxy);
}
var regex = /([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}):([0-9]{2,8})/gm;
var m;
var result = ""
while ((m = regex.exec(proxies_summary)) !== null) {
    if (m.index === regex.lastIndex) {
        regex.lastIndex++;
    }
    if (typeof m[2] != "undefined"){
    result += `${m[1]}:${m[2]}\n`;
    }
}

return result;