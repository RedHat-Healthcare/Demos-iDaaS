create table if not exists refdata_status
(
	statusid smallint auto_increment
		primary key,
	statusdesc varchar(25) null,
	createdDate timestamp default CURRENT_TIMESTAMP null
);

create table if not exists appdata_auditlog
(
	auditlogid bigint auto_increment
		primary key,
	msgprocesseddate varchar(14) null,
	msgprocessedtime varchar(14) null,
	processedtype varchar(10) null,
	industrystd varchar(10) null,
	componentname varchar(45) null,
	msgtrigger varchar(20) null,
	msgid varchar(25) null,
	internalID varchar(38) null,
	createddate timestamp default CURRENT_TIMESTAMP null,
	statusid smallint default 1 null,
	constraint FK_appdata_auditlog_status
		foreign key (statusid) references refdata_status (statusid)
);

create index IX_appdata_auditlog
	on appdata_auditlog (auditlogid, msgprocesseddate, msgprocessedtime, processedtype, industrystd, componentname, msgtrigger, msgid, internalID, createddate, statusid);

create table if not exists appdata_auditlog_msgs
(
	auditlogmsgsid bigint auto_increment
		primary key,
	msgid int null,
	msgdata text null,
	msgdatalink varchar(45) null,
	createdDate timestamp default CURRENT_TIMESTAMP null,
	statusid smallint default 1 null,
	constraint FK_appdata_auditlog_msgs_status
		foreign key (statusid) references refdata_status (statusid)
);

create index IX_appdata_auditlog_msgs
	on appdata_auditlog_msgs (auditlogmsgsid, msgid, msgdatalink, createdDate, statusid);

create table if not exists refdata_industrystd
(
	industrystd varchar(10) not null
		primary key,
	industrystddesc varchar(35) null,
	CreatedDate timestamp default CURRENT_TIMESTAMP null,
	statusid smallint default 1 null,
	constraint IX_refdata_industrystd_status
		foreign key (statusid) references refdata_status (statusid)
);

create table if not exists refdata_msgtrigger
(
	msgtriggerid varchar(20) not null
		primary key,
	msgtriggerdesc varchar(50) null,
	createdDate timestamp default CURRENT_TIMESTAMP null,
	statusid smallint default 1 null,
	constraint FK_refdata_msgtrigger_status
		foreign key (statusid) references refdata_status (statusid)
);

create index IX_refdata_msgtrigger
	on refdata_msgtrigger (msgtriggerid, msgtriggerdesc, createdDate, statusid);

create table if not exists refdata_processingtype
(
	processingtypeID varchar(10) not null
		primary key,
	processingtypedesc varchar(40) null,
	createdDate timestamp default CURRENT_TIMESTAMP null,
	statusid smallint default 1 null,
	constraint FK_refdata_processingtype_status
		foreign key (statusid) references refdata_status (statusid)
);

create index IX_refdata_processingtype
	on refdata_processingtype (processingtypeID, processingtypedesc, createdDate, statusid);

create index IX_refdata_status
	on refdata_status (statusid, statusdesc, createdDate);

