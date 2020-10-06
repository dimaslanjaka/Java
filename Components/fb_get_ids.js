var html = document.getElementsByTagName('html')[0].innerHTML;
var match = html.match(/ft_id\=([0-9]{5,20})\&/gm);
document.getElementsByTagName('html')[0].innerHTML.match(/ft_id\=([0-9]{5,20})\&/gm);

//tanggapan</h1>
document.getElementsByTagName('html')[0].innerHTML.split(/tanggapan</h1>(.*?)@@/).split('<div id="static')
document.getElementsByTagName('html')[0].innerHTML.split(/tanggapan\<\/h1\>(.*?)<div id="static/gm)