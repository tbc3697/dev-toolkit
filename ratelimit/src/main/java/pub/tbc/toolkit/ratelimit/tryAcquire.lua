-- 存放上一次请求后剩余数量的KEY
local count_key = KEYS[1]
-- 存放上一次请求时间
local time_key = KEYS[2]

-- 本次所需令牌数量
local requiredCount = ARGS[1]
-- 令牌产生速度，个/毫秒
local velocity = ARGS[2]

-- 剩余令牌数量
local currentTokenCount = redis.call("GET", "KEY[1]")
if (currentTokenCount == nil) {
    currentTokenCount = 0
}

-- 新的数量
local newCount = 0
if (currentTokenCount >= requiredCount) {
    newCount = currentTokenCount - requiredCount;
}

-- local time = 当前时间