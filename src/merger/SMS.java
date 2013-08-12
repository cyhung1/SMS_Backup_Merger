package merger;

public class SMS {
	/*
	 * <sms protocol="0" address="7123712845" date="1353889039160" type="1"
	 * subject="null" body="aGonna miss my Xbox for these next few weeks."
	 * toa="null" sc_toa="null" service_center="null" read="1" status="-1"
	 * locked="0" date_sent="null" readable_date="Nov 25, 2012 6:17:19 PM"
	 * contact_name="Nickolas Anthony Johnathon Homan" />
	 */

	private String protocol, address, date, type, subject, body, toa, sc_toa, service_center, read, status, locked,
			date_sent, readable_date, contact_name;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getToa() {
		return toa;
	}

	public void setToa(String toa) {
		this.toa = toa;
	}

	public String getSc_toa() {
		return sc_toa;
	}

	public void setSc_toa(String sc_toa) {
		this.sc_toa = sc_toa;
	}

	public String getService_center() {
		return service_center;
	}

	public void setService_center(String service_center) {
		this.service_center = service_center;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getDate_sent() {
		return date_sent;
	}

	public void setDate_sent(String date_sent) {
		this.date_sent = date_sent;
	}

	public String getReadable_date() {
		return readable_date;
	}

	public void setReadable_date(String readable_date) {
		this.readable_date = readable_date;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String toString() {
		return "Protocol: " + protocol + "address: " + address + "date: " + date + "type: " + type + "subject: "
				+ subject + "body: " + body + "toa: " + toa + "sc_toa: " + sc_toa + "service_center: " + service_center
				+ "read: " + read + "status: " + status + "locked: " + locked + "date_sent: " + date_sent
				+ "readable_date: " + readable_date + "contact_name: " + contact_name;
	}
}
