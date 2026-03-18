create table if not exists sys_func_process (
    id varchar(64) primary key,
    func_id varchar(64) not null,
    bean_name varchar(128) not null,
    constraint uk_sys_func_process_func_id unique (func_id)
);
