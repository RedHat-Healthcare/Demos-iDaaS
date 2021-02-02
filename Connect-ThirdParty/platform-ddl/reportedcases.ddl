create table if not exists reportedcases
(
	ReportedCasesID bigint auto_increment
		primary key,
	Organization varchar(10) null,
	PatientAccount varchar(15) null,
	PatientName varchar(75) null,
	ZipCode varchar(10) null,
	RoomBed varchar(10) null,
	Age smallint null,
	Gender varchar(1) null,
	AdmissionDate varchar(12) null,
	CreatedDate timestamp default CURRENT_TIMESTAMP null,
	StatusID smallint default 1 null
);

create index IDX_reportedcases
	on reportedcases (ReportedCasesID, Organization, PatientAccount, PatientName, ZipCode, RoomBed, Age, Gender, AdmissionDate, CreatedDate, StatusID);