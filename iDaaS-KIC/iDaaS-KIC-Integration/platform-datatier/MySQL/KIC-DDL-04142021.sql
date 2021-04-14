create table if not exists kic.insight
(
    auditmessageID bigint auto_increment
    primary key,
    messageprocesseddate varchar(25) null,
    messageprocessedtime varchar(25) null,
    processingtype varchar(25) null,
    industrystd varchar(20) null,
    component varchar(75) null,
    messagetrigger varchar(25) null,
    processname varchar(55) null,
    auditdetails varchar(99) null,
    camelid varchar(49) null,
    exchangeid varchar(49) null,
    internalmsgid varchar(49) null,
    auditentiremessage text null,
    bodydata text null
    );

create index IDX_Insight
	on kic.insight (auditmessageID, messageprocesseddate, messageprocessedtime, processingtype, industrystd, component, messagetrigger, processname, auditdetails, camelid, exchangeid, internalmsgid);

