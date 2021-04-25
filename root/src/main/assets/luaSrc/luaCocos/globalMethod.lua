
require "cocos.init"

globalMethod = class("globalMethod")
globalMethod.__index = globalMethod


globalMethod.s_visibleRect = cc.rect(0,0,0,0)
globalMethod.g_iShowGameRule = 0

cclog = function(...)
    --print(string.format(...))
    release_print(string.format(...))
end
--for debug in vscode

--for debug end
-- for CCLuaEngine traceback
function __G__TRACKBACK__(msg)
    local errMsg1 = "LUA ERROR: " .. tostring(msg) .. "\n"
    local errMsg2 = debug.traceback()
    local errLogFile = nil
    if fdLogic.LogicBase:shareLogicLayer():getLobbyInfo().cUsection=="test" then
        local errLogFileName = cc.FileUtils:getInstance():getWritablePath().."errLogFile.txt"
        errLogFile = io.open(errLogFileName, "a")
    else
        local iTemp = gccommon.GCLocalData:sharedLocalData():getIntegerForKey("t_s_e_l")
        if iTemp~=nil and  iTemp==1 then
            local errLogFileName = cc.FileUtils:getInstance():getWritablePath().."errLogFile.txt"
            errLogFile = io.open(errLogFileName, "a")
        end
    end
    if errLogFile~=nil then
        io.output(errLogFile)
        io.write("\n----------------------------------------\n")
        io.write(os.date().."\n")
        io.write(errMsg1)
        io.write(errMsg2)
        io.write("\n----------------------------------------\n")
        io.flush()
        io.close(errLogFile)
    end
    cclog("----------------------------------------")
    cclog(errMsg1)
    cclog(errMsg2)
    cclog("----------------------------------------")

    -- for debug in vscode
    if breakSocketHandle ~= nil then
            print("LUA ERROR: " .. tostring(msg))
            print(debug.traceback())
            debugXpCall()
    end
    -- for debug in vscode end
    -- report lua exception
    -- buglyReportLuaException(tostring(msg), debug.traceback())
end

function globalMethod:lazyInit()
    if (self.s_visibleRect.width == 0.0 and self.s_visibleRect.height == 0.0) then
        self.s_visibleRect.x = 0
        self.s_visibleRect.y = 0
        local size  = cc.Director:getInstance():getOpenGLView():getDesignResolutionSize()
        self.s_visibleRect.width  = size.width
        self.s_visibleRect.height = size.height
    end
end

function globalMethod:WinCenter()
    self:lazyInit()
    return cc.p(self.s_visibleRect.x+self.s_visibleRect.width/2, self.s_visibleRect.y+self.s_visibleRect.height/2)
end

function globalMethod:WinRect()
    self:lazyInit()
    return cc.rect(self.s_visibleRect.x, self.s_visibleRect.y, self.s_visibleRect.width, self.s_visibleRect.height)
end

--拼接utf_8字符串
function globalMethod:utf8_from(t)
  local bytearr = {}
  for _, v in ipairs(t) do
    local utf8byte = v < 0 and (0xff + v + 1) or v
    table.insert(bytearr, string.char(utf8byte))
  end
  return table.concat(bytearr)
end

function globalMethod:BufferSkipMsgHead(data)
  data:MovePosForStr(4)
  data:MovePosForShort(2)
  data:MovePosForInt(1)
end

function hex2bin( hexstr )  
    local str = ""  
    for i = 1, string.len(hexstr) - 1, 2 do    
        local doublebytestr = string.sub(hexstr, i, i+1);    
        local n = tonumber(doublebytestr, 16);    
        if 0 == n then    
            str = str .. '\00'  
        else    
            str = str .. string.format("%c", n)  
        end  
    end   
    return str  
end
local g_UserDefaultInstance = nil
function checkUserDefaultInstance()
    if g_UserDefaultInstance == nil then g_UserDefaultInstance = cc.UserDefault:getInstance() end
end
function getUserDefault(args, valueType)
    checkUserDefaultInstance()
    local ret = nil
    if valueType==0 then
        ret = g_UserDefaultInstance:getIntegerForKey(args)
    else
        ret = g_UserDefaultInstance:getStringForKey(args)
    end
    return ret
--    local xmlPath = cc.UserDefault:getXMLFilePath()
--    local xmlFile = io.open(xmlPath, "r")
--    local allText = xmlFile:read("*a")
--    xmlFile:close()
--    local beginIndex,endIndex = string.find(allText,args)
--    local nextIndex = string.find(allText,"<", endIndex)
--    local strText = string.sub(allText, endIndex+2, nextIndex-1)
--    --print("getUserDefault:"..args.."="..strText)
--    if valueType==0 then
--        return tonumber(strText)
--    end
--    return strText
end
function setUserDefault(args, valueType, value)
    checkUserDefaultInstance()
    if valueType==0 then
        g_UserDefaultInstance:setIntegerForKey(args, value)
    else
        g_UserDefaultInstance:setStringForKey(args, value)
    end
    g_UserDefaultInstance:flush()
end

--- Deep copies a table into a new table.
-- Tables used as keys are also deep copied, as are metatables
-- @param orig The table to copy
-- @return Returns a copy of the input table
function deep_copy(orig)
  local copy
  if type(orig) == "table" then
    copy = {}
    for orig_key, orig_value in next, orig, nil do
      copy[deep_copy(orig_key)] = deep_copy(orig_value)
    end
    setmetatable(copy, deep_copy(getmetatable(orig)))
  else
    copy = orig
  end
  return copy
end

--- Copies a table into a new table.
-- neither sub tables nor metatables will be copied.
-- @param orig The table to copy
-- @return Returns a copy of the input table
function shallow_copy(orig)
  local copy
  if type(orig) == "table" then
    copy = {}
    for orig_key, orig_value in pairs(orig) do
      copy[orig_key] = orig_value
    end
  else -- number, string, boolean, etc
    copy = orig
  end
  return copy
end

function getIntByBit(bitArr, beginIndex, bitNum)
    local rt = 0
    for j=0,bitNum-1 do
        if bitArr[j+beginIndex] == 1 then
            rt = rt + bit.data32[32-j]
        end
    end
    return rt
end





