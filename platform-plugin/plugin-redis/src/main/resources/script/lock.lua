---
-- 加锁脚本
---
local result = redis.call("SETNX", KEYS[1] , ARGV[1])
if result == 1
then
    return redis.call("EXPIRE", KEYS[1] , ARGV[2])
else
    return 0
end