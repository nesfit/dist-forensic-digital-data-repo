# Distributed Forensic Digital Data Repository: User Guide

(c) 2018-2020 Marek Rychly (rychly@fit.vutbr.cz) and Martin Josefik (xjosef00@stud.fit.vutbr.cz)

## Commands

To control the Distributed Repository, clients can use a Kafka message queue published by the repository.

### Request

Each message sent by a client to the repository must consist of:

*	command -- Specifies the type of command which is one of STORE_PCAP and LOAD_PCAP.
	The command also encapsulates the type of operation, which can be SAVE and LOAD,
	as well as the type of data such as PCAP, Packet, BINARY, LOG, etc.
*	id -- Mandatory parameter, specifies a unique message ID so that the client can
	identify the already processed orders.
*	awaitsResponse -- Two-state value if the client/sender of the message expects a response
	from the repository. If this parameter is specified as True, another "responseTopic" parameter
	must also be specified.
*	responseTopic -- Specifies the name of the queue (topic) in which the response on the client
	side is expected.
*	errorTopic -- An error may occur on the distributed storage side, such as database
	unavailability, command processing error, and more. This parameter is used to specify
	the queue to which error messages will be sent. This is a required parameter.
*	dataSource -- Source data to be stored can be sent in two ways. The first is to send data
	in binary form directly via the Kafka system together with the command.
	This method can be used for data that is not too large because the data is loaded into RAM.
	Such an approach is not suitable for large volumes of data. Therefore, there is a second way,
	and that is to pass data over a distributed HDFS file system. The "dataSource" parameter
	contains the name of the repository in the form of an enumeration constant of Kafka or HDFS,
	the path to the file if it is HDFS, and also the "removeAfterUse" attribute to remove
	the file after use. It can also be used for data reading commands to transfer the result,
	via HDFS or Kafka.
*	criterias -- Represents a list of criteria for querying. This parameter should be filled
	in only when sending a read command.

### Reply

After processing a request by the Distributed repository, the system sends a reply, if requested
(see the "awaitsResponse" parameter of the client's request above). The reply consists of:

*	id -- This is a unique ID copied from the request message. After receiving the response,
	the client will know to which request the response belongs.
*	responseTopic -- The name of the output queue to which the response is sent.
*	responseCode -- The response return code symbolizing how the operation turned out.
	Analogous with HTTP return codes, possible values are: OK (200), BAD_REQUEST (400),
	UNSUPPORTED_MEDIA_TYPE (415), INTERNAL_SERVER_ERROR (500).
*	status -- Reserved parameter for response status.
*	detailMessage -- If an error occurs during the processing of the request,
	the answer can be sent why the error occurred or its cause.

## Demonstration Example

To develop a client application, which uses the Kafka message queue to control the repository,
see [the Producer Demo application](../apps/ProducerDemo).

## Acknowledgements

*This work was supported by the Ministry of the Interior of the Czech Republic as a part of the project Integrated platform for analysis of digital data from security incidents VI20172020062.*
