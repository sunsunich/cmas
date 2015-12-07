package cz.atria.barcode;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: ABadretdinov
 * Date: 01.08.13
 * Time: 19:18
 */
public class CalendarEvent
{
	private String summary;
	private Date startDate;
	private Date endDate;
	private String location;
	private String description;
	private static final String DATE_FORMAT="yyyyMMdd'T'HHmmss'Z'";
	private SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_FORMAT);

	public CalendarEvent(String summary, Date startDate, Date endDate, String location, String description)
	{
		this();
		this.summary = summary;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.description = description;
	}

	public CalendarEvent()
	{
		super();
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	public String toString()
	{
		StringBuilder builder=new StringBuilder("");
		builder.append("BEGIN:VEVENT \n");
		if(!TextUtils.isEmpty(summary))
		{
			builder.append("SUMMARY:");
			builder.append(summary);
			builder.append(" \n");
		}
		if(startDate!=null)
		{
			builder.append("DTSTART:");
			builder.append(dateFormat.format(startDate));
			builder.append(" \n");
		}
		if(endDate!=null)
		{
			builder.append("DTEND:");
			builder.append(dateFormat.format(endDate));
			builder.append(" \n");
		}
		if(!TextUtils.isEmpty(location))
		{
			builder.append("LOCATION:");
			builder.append(location);
			builder.append(" \n");
		}
		if(!TextUtils.isEmpty(description))
		{
			builder.append("DESCRIPTION:");
			builder.append("Врач: ").append(description);
			builder.append(" \n");
		}
		builder.append("END:VEVENT \n");
		return builder.toString();
	}

}
