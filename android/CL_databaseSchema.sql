BEGIN TRANSACTION;
CREATE TABLE call_forwarding (time NUMERIC, forwarding_enabled NUMERIC);
CREATE TABLE call_state (time NUMERIC, state NUMERIC);
CREATE TABLE cell_location (time NUMERIC, cellLocation TEXT);
CREATE TABLE data_connection_state (type NUMERIC, time NUMERIC, state NUMERIC);
CREATE TABLE service_state (time NUMERIC, value NUMERIC);
CREATE TABLE signal_strength (time NUMERIC, gsmSignal NUMERIC, gsmBitErrorRate NUMERIC);
CREATE TABLE wifi_onoff (time NUMERIC, value NUMERIC);
COMMIT;
