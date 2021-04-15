<?php

if (!isLocalHost()) {
  //error_reporting(0);
}
session_start();
setcookie('ip', get_client_ip(), 230444, "/");
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

$jsonLocation = __DIR__ . '/servers.json';
if (!file_exists($jsonLocation)) {
  file_put_contents($jsonLocation, json_encode([]));
}
$json = json_decode(file_get_contents($jsonLocation), true);

$write = false;
if (isset($_REQUEST['server'])) {
  $url_repository = urldecode(trim($_REQUEST['server']));
  $status = null;
  $parse = parse_url($url_repository);
  $validate = false;
  $host = null;

  /*
  if (isLocalHost()) {
    try {
      list($status) = get_headers($_REQUEST['server']);
    } catch (\Throwable $th) {
      $status = null;
    }

    if (isset($parse['host'])) {
      $host = gethostbyname($parse['host']);
    } else {
      $host = $url_repository;
    }
    $validate = filter_var($host, FILTER_VALIDATE_IP);
  } else {
    $validate = isValidUrl($url_repository);
    if (!$validate) {
      $validate = isset($_SERVER['HTTP_USER_AGENT']);
    }
  }
  */

  if (get_client_ip() && filter_var($url_repository, FILTER_VALIDATE_URL)) {
    if (!isset($json[get_client_ip()])) {
      $json[get_client_ip()] = [];
    }
    $json[get_client_ip()][] = $url_repository;
    $write = true;
  }

  //$json['validate'] = [$_REQUEST['server'], $validate, $status, $host];
  //$json['server-info'] = $_SERVER;

}

if (isset($_REQUEST['url']) && filter_var(urldecode($_REQUEST['url']), FILTER_VALIDATE_URL)) {
  if (!isset($_REQUEST['name'])) {
    $write = true;
    $json[get_client_ip()][] = urldecode($_REQUEST['url']);
  } else {
    $write = true;
    $json[strtolower(urldecode($_REQUEST['name']))][] = urldecode($_REQUEST['url']);
  }
}

if ($write) {
  $json = array_unique_recursive($json);
  file_put_contents($jsonLocation, json_encode($json));
}

$repotutorial = (isset($_SERVER['HTTPS']) && 'on' === $_SERVER['HTTPS'] ? 'https' : 'http') . "://$_SERVER[HTTP_HOST]/servers/index.php";
$json['__init__'] = <<<EOF
to add your server, insert like this into your browser {$repotutorial}?server=http(s)://yourdomain.com/repo/path, to only get repositories {$repotutorial}?repoOnly, url repository: https://github.com/dimaslanjaka/gradle-plugin
EOF;
ksort($json);

if (isset($_REQUEST['repoOnly'])) {
  $rjson = [];
  foreach ($json as $key => $value) {
    if (is_array($json[$key])) {
      foreach (array_values($json[$key]) as $repo) {
        $rjson[] = $repo;
      }
    }
  }
  $json = $rjson;
}


echo json_encode($json, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);


function array_unique_recursive($array)
{
  $array = array_unique($array, SORT_REGULAR);

  foreach ($array as $key => $elem) {
    if (is_array($elem)) {
      $array[$key] = array_unique_recursive($elem);
    }
  }

  return $array;
}

/**
 * Detect is localhost
 *
 * @return boolean
 */
function isLocalHost()
{
  $whitelist = [
    '127.0.0.1',
    '::1',
  ];

  return in_array($_SERVER['REMOTE_ADDR'], $whitelist);
}

/**
 * Get client ip, when getenv supported (maybe cli)
 *
 * @return string
 */
function get_client_ip()
{
  $ipaddress = '';

  if (isLocalHost()) {
    $ipaddress = getLocalIp();
  } else {
    if (getenv('HTTP_CLIENT_IP')) {
      $ipaddress = getenv('HTTP_CLIENT_IP');
    } elseif (getenv('HTTP_X_FORWARDED_FOR')) {
      $ipaddress = getenv('HTTP_X_FORWARDED_FOR');
    } elseif (getenv('HTTP_X_FORWARDED')) {
      $ipaddress = getenv('HTTP_X_FORWARDED');
    } elseif (getenv('HTTP_FORWARDED_FOR')) {
      $ipaddress = getenv('HTTP_FORWARDED_FOR');
    } elseif (getenv('HTTP_FORWARDED')) {
      $ipaddress = getenv('HTTP_FORWARDED');
    } elseif (getenv('REMOTE_ADDR')) {
      $ipaddress = $ipaddress = getenv('REMOTE_ADDR');
    } else {
      /**
       * Return to method 2
       */
      $ipaddress = get_client_ip2();
    }
  }

  return $ipaddress;
}

/**
 * Get client ip, when running on webserver
 *
 * @return void
 */
function get_client_ip2()
{
  $ipaddress = '';
  if (isLocalHost()) {
    $ipaddress = getLocalIp();
  } else {
    if (isset($_SERVER['HTTP_CLIENT_IP'])) {
      $ipaddress = $_SERVER['HTTP_CLIENT_IP'];
    } elseif (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
      $ipaddress = $_SERVER['HTTP_X_FORWARDED_FOR'];
    } elseif (isset($_SERVER['HTTP_X_FORWARDED'])) {
      $ipaddress = $_SERVER['HTTP_X_FORWARDED'];
    } elseif (isset($_SERVER['HTTP_FORWARDED_FOR'])) {
      $ipaddress = $_SERVER['HTTP_FORWARDED_FOR'];
    } elseif (isset($_SERVER['HTTP_FORWARDED'])) {
      $ipaddress = $_SERVER['HTTP_FORWARDED'];
    } elseif (isset($_SERVER['REMOTE_ADDR'])) {
      $ipaddress = $_SERVER['REMOTE_ADDR'];
    } else {
      $ipaddress = 'UNKNOWN';
    }
  }

  return $ipaddress;
}

function getLocalIp()
{
  if (defined('PHP_MAJOR_VERSION') && PHP_MAJOR_VERSION >= 5) {
    $localIP = gethostbyname(gethostname());
  } else {
    $localIP = gethostbyname(php_uname('n'));
  }

  return $localIP;
}


// method 2
function isValidUrl($url)
{
  // first do some quick sanity checks:
  if (!$url || !is_string($url)) {
    return false;
  }
  // quick check url is roughly a valid http request: ( http://blah/... )
  if (!preg_match('/^http(s)?:\/\/[a-z0-9-]+(\.[a-z0-9-]+)*(:[0-9]+)?(\/.*)?$/i', $url)) {
    return false;
  }
  // the next bit could be slow:
  if (getHttpResponseCode_using_curl($url) != 200) {
    //      if(getHttpResponseCode_using_getheaders($url) != 200){  // use this one if you cant use curl
    return false;
  }
  // all good!
  return true;
}

function getHttpResponseCode_using_curl($url, $followredirects = true)
{
  // returns int responsecode, or false (if url does not exist or connection timeout occurs)
  // NOTE: could potentially take up to 0-30 seconds , blocking further code execution (more or less depending on connection, target site, and local timeout settings))
  // if $followredirects == false: return the FIRST known httpcode (ignore redirects)
  // if $followredirects == true : return the LAST  known httpcode (when redirected)
  if (!$url || !is_string($url)) {
    return false;
  }
  $ch = @curl_init($url);
  if ($ch === false) {
    return false;
  }
  @curl_setopt($ch, CURLOPT_HEADER, true);    // we want headers
  @curl_setopt($ch, CURLOPT_NOBODY, true);    // dont need body
  @curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);    // catch output (do NOT print!)
  if ($followredirects) {
    @curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    @curl_setopt($ch, CURLOPT_MAXREDIRS, 10);  // fairly random number, but could prevent unwanted endless redirects with followlocation=true
  } else {
    @curl_setopt($ch, CURLOPT_FOLLOWLOCATION, false);
  }
  //      @curl_setopt($ch, CURLOPT_CONNECTTIMEOUT ,5);   // fairly random number (seconds)... but could prevent waiting forever to get a result
  //      @curl_setopt($ch, CURLOPT_TIMEOUT        ,6);   // fairly random number (seconds)... but could prevent waiting forever to get a result
  //      @curl_setopt($ch, CURLOPT_USERAGENT      ,"Mozilla/5.0 (Windows NT 6.0) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");   // pretend we're a regular browser
  @curl_exec($ch);
  if (@curl_errno($ch)) {   // should be 0
    @curl_close($ch);
    return false;
  }
  $code = @curl_getinfo($ch, CURLINFO_HTTP_CODE); // note: php.net documentation shows this returns a string, but really it returns an int
  @curl_close($ch);
  return $code;
}

function getHttpResponseCode_using_getheaders($url, $followredirects = true)
{
  // returns string responsecode, or false if no responsecode found in headers (or url does not exist)
  // NOTE: could potentially take up to 0-30 seconds , blocking further code execution (more or less depending on connection, target site, and local timeout settings))
  // if $followredirects == false: return the FIRST known httpcode (ignore redirects)
  // if $followredirects == true : return the LAST  known httpcode (when redirected)
  if (!$url || !is_string($url)) {
    return false;
  }
  $headers = @get_headers($url);
  if ($headers && is_array($headers)) {
    if ($followredirects) {
      // we want the last errorcode, reverse array so we start at the end:
      $headers = array_reverse($headers);
    }
    foreach ($headers as $hline) {
      // search for things like "HTTP/1.1 200 OK" , "HTTP/1.0 200 OK" , "HTTP/1.1 301 PERMANENTLY MOVED" , "HTTP/1.1 400 Not Found" , etc.
      // note that the exact syntax/version/output differs, so there is some string magic involved here
      if (preg_match('/^HTTP\/\S+\s+([1-9][0-9][0-9])\s+.*/', $hline, $matches)) { // "HTTP/*** ### ***"
        $code = $matches[1];
        return $code;
      }
    }
    // no HTTP/xxx found in headers:
    return false;
  }
  // no headers :
  return false;
}
