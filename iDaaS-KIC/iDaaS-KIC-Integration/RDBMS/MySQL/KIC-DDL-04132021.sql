use kic;
create table if not exists insight
(
    auditmessageID bigint not null AUTO_INCREMENT,
    messageprocesseddate varchar(25),
    messageprocessedtime varchar(25),
    processingtype varchar(25),
    industrystd varchar(20),
    component varchar(75),
    messagetrigger varchar(25),
    processname varchar(55),
    auditdetails varchar(99),
    camelid varchar(49),
    exchangeid varchar(49),
    internalmsgid varchar(49),
    auditentiremessage text,
    bodydata text,
    PRIMARY KEY (`auditmessageID`)
    );