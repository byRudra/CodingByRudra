# Write your MySQL query statement below
Select tweet_id
from Tweets
WHERE CHAR_LENGTH(content) > 15;