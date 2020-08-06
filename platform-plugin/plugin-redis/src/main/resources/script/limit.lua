---
-- 限流脚本 {KEYS[1]: key,  ARGV[1]: 速率, ARGV[2]: key过期时间}
---

-- 当前流量
local current = tonumber(redis.call("INCRBY", KEYS[1], 1))
-- cnt 值为1说明是第一次, 因此需要设置其过期时间
if (current == 1)
then
    redis.call("EXPIRE", KEYS[1], tonumber(ARGV[2]))
elseif (current > tonumber(ARGV[1]))
then
    return 0
end
return 1