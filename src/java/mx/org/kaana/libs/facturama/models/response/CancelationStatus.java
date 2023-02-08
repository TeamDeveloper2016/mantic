
package mx.org.kaana.libs.facturama.models.response;


public class CancelationStatus 
{
       
    private String Status;
    private String Message;
    private String IsCancelable;
    private String Uuid;
    private String RequestDate;
    private String ExpirationDate;
    private String AcuseXmlBase64;
    private String CancelationDate;    
    
    
   public String getStatus()
   {
        return Status;
   }
   
   public void setStatus(String Status)
   {
      this.Status = Status;
   }
   
   public String getMessage()
   {
        return Message;
   }
   
   public void setMessage(String Message)
   {
      this.Message = Message;
   }
      
    public String getIsCancelable()
   {
        return IsCancelable;
   }
   
   public void setIsCancelable(String IsCancelable)
   {
      this.IsCancelable = IsCancelable;
   }
   
    public String getUuid()
   {
        return Uuid;
   }
   
   public void setUuid(String Uuid)
   {
      this.Uuid = Uuid;
   }
   
       public String getExpirationDate()
   {
        return ExpirationDate;
   }
   
   public void setExpirationDate(String ExpirationDate)
   {
      this.ExpirationDate = ExpirationDate;
   }
   
   public String getRequestDate()
   {
        return RequestDate;
   }
   
   public void setRequestDate(String RequestDate)
   {
      this.RequestDate = RequestDate;
   }
   
   public String getAcuseXmlBase64()
   {
        return AcuseXmlBase64;
   }
   
   public void setAcuseXmlBase64(String AcuseXmlBase64)
   {
      this.AcuseXmlBase64 = AcuseXmlBase64;
   }
   
   public String getCancelationDate()
   {
        return CancelationDate;
   }
   
   public void setCancelationDate(String CancelationDate)
   {
      this.CancelationDate = CancelationDate;
   }
  
}
