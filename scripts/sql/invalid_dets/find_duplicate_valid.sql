select receiver_name, transmitter_id, timestamp
from valid_detection
group by receiver_name, transmitter_id, timestamp
having count(*) > 1;

