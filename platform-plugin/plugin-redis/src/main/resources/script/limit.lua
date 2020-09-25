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



-- 计数器限流
-- 此处支持的最小单位时间是秒, 若将 expire 改成 pexpire 则可支持毫秒粒度.
-- KEYS[1]  string  限流的key
-- ARGV[1]  int     限流数
-- ARGV[2]  int     单位时间(秒)

-- local cnt = tonumber(redis.call("incr", KEYS[1]))

-- if (cnt == 1) then
    -- cnt 值为1说明之前不存在该值, 因此需要设置其过期时间
--     redis.call("expire", KEYS[1], tonumber(ARGV[2]))
-- elseif (cnt > tonumber(ARGV[1])) then
--     return -1
-- end

-- return cnt