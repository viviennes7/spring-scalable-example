DROP SCHEMA IF EXISTS spring_scalable;
CREATE SCHEMA spring_scalable;
USE spring_scalable;

create table spring_scalable.event_order
(
	id bigint auto_increment
		primary key,
	create_date datetime null,
	item_id bigint null,
	update_date datetime null,
	user_id bigint null
)
engine=MyISAM;



