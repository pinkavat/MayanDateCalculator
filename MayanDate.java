import java.util.Calendar;

/*
 * Thomas Pinkava's Mayan Date Format and Calendrical Suite
 * Includes Long Count, Tzolk'in, Haab', and NonLunar Supplementary Series
 * Written July of 2016
 * 
 * Constructors:
 * MayanDate(int[] longCount) -- Sets the date to the given 5-digit standard Long Count. Nonstandard dates will default to 0.
 * MayanDate(int days) -- Sets the date to the given MDC (number of days since August 11, 3114 BCE).
 * MayanDate(int day, int month, int year, boolean bc) -- Sets the date to the given Gregorian date.
 * MayanDate(int day, int month, int year) -- Sets the date to the given astronomical Gregorian date.
 * MayanDate() -- Sets the date to the current date.
 * 
 * Accessors:
 * int[] getTzolkin() -- Returns the Tzolk'in [Position, Trecena, Veintena].
 * int[] getHaab() -- Returns the Haab' [Position, Day, Month].
 * int[] getEight() -- Returns the 819-day cycle [Station Distance, Station Day, Color&Quadrant].
 * int[] getSupp() -- Returns the NonLunar Supplementary Series [Lord of the Night, Year Bearer Number, Year Bearer Name (Tzolkin), Seven-Day Cycle]
 * int[] getLongCount() -- Returns the standard Long Count [Bak'tun, K'atun, Tun, Winal, K'in]
 * int[] getGregDate() -- Returns the Gregorian Date [day, month, year]. Note: the year is astronomical (BCE dates are negative numbers).
 * int getMDC() -- Returns the MDC (number of days since August 11, 3114 BCE).
 * int getCRD() -- Returns the position within the Calendar Round.
 * String toString() -- Returns a String containing the Long Count, Tzolk'in, and Haab'.
 * String lcToString() -- Returns a String containing the Long Count.
 * String crToString() -- Returns a String containing the Tzolk'in and Haab'.
 * String eightToString() -- Returns a String containing the 819-day cycle Quadrant and Color.
 * String suppToString() -- Returns a String containing the NonLunar Supplementary Series.
 * String gregToString() -- Returns a String containing the Gregorian Date.
 * 
 * Mutators:
 * void setMDC(int days) -- Sets the date to the given MDC (number of days since August 11, 3114 BCE).
 * void setGregDate(int day, int month, int year, boolean bc) -- Sets the date to the given Gregorian date.
 * void setGregDate(int day, int month, int year) -- Sets the date to the given astronomical Gregorian date.
 * void setLongCount() -- Sets the date to the given 5-digit standard Long Count. Nonstandard dates will default to 0.
 * void today() -- Sets the date to the current date. 
 * void oneUp() -- Increments the day by 1.
 * void oneDown() -- Decrements the day by 1.
 * 
 * 
 * Public Method Toolbox:
 * 
 * int[] getDistance(int[] firstDate, int[] secondDate) -- Returns the Distance Number (in signed Long Count form) between Long Counts firstDate and secondDate.
 * int[] getDistance(int firstDate, int secondDate) -- Ditto, but for MDC instead of Long Counts.
 * int[] giveDistance(int[] firstDate, int[] distance) -- Returns the new Long Count given by starting at firstDate and adding distance.
 * int[] giveDistance(int firstDate, int distance) -- Ditto, but for MDC instead of Long Counts.
 * 
 * int gregMDC(int day, int month, int year) -- Converts Gregorian date to MDC. Note: the year is astronomical (BCE dates give negative numbers).
 * int[] mdcGREG(int day) -- Converts given MDC to Gregorian [day, month, year].
 * int[] longCount(int day) -- Converts given MDC to standard Long Count Format [Bak'tun, K'atun, Tun, Winal, K'in].
 * int unLongCount(int[] l) -- Converts given standard Long Count [Bak'tun, K'atun, Tun, Winal, K'in] to MDC.
 * 
 * int[] tzolkinOf(int day) -- Returns the Tzolk'in [Position, Trecena, Veintena] of the given MDC.
 * int[] haabOf(int day) -- Returns the Haab' [Position, Day, Month] of the given MDC.
 * 
 * int[] roundBorders(int day) -- Calculates the Beginning and Ending MDC values of the Calendar Round containing the given MDC.
 * int[] estimate(int round, int n) -- Calculates n possible MDC values corresponding to a Calendar Round value, starting at 0.
 * int[] estimate(int round, int n, int g) -- Calculates n possible MDC values corresponding to a Calendar Round value, starting at 0, given a Lord of The Night g.
 * int roundPos(int tn, int td, int hn, int hm) -- Calculates the position in the Calendar Round from Tzolk'in and Haab' values.
 * 
 * String lcCond(int[] longCount) -- Converts a standard Long Count to a useable String.
 * String crCond(int[] tzolkin, int[] haab) -- Converts a Calendar Round pair into a useable String.
 * String gregCond(int[] greg) -- Converts a Gregorian Date into a useable String.
 * 
 * String omenYear(int ynum, int yname) -- Returns a String containing the Year Bearer's Omens given a Year Bearer with number ynum and name yname.
 * String omenDay(int tzolname) -- Returns a String containing the Tzolkin Day's Omens given the Tzolkin name tzolname. 
 */


public class MayanDate {
  
  //Constants -------------------------------------------------------------
  
  private int C_CONS = 584283; //Correlation Constant (set to GMT)
  private String[] TZ_NAMES = {"Ajaw","Imix","Ik'","Ak'bal","K'an","Chikchan","Kimi","Manik","Lamat","Muluk","Ok","Chuwen","Eb'","Ben","Ix","Men","Kib'","Kab'an","Etz'nab'","Kawak"};
  private String[] HA_NAMES = {"Pop","Wo","Sip","Sots'","Sek","Xul","Yaxk'in","Mol","Ch'en","Yax","Sak","Keh","Mak","K'ank'in","Muwan","Pax","K'ayab","Kumk'u","Wayeb"};
  private String[] EI_QUADRANTS = {"Elk'ihn","Xaman","Ochk'ihn","Nojo'l","East","North","West","South"};
  private String[] EI_COLORS = {"Chak","Sak","Ik'","Kan","Red","White","Black","Yellow"};
  private String[] GR_MONTHS = {"ERROR","January","February","March","April","May","June","July","August","September","October","November","December"};
  private String[] DAY_PROPHECIES = {"The Day of Rulers:\nFavorable for offering candles.",
    "The Day of Earth:\nFavorable for praying on behalf of home and family.",
    "The Day of Wind:\nFavorable for sowing corn.",
    "The Day of Darkness:\nFavorable for doing evil to others.",
    "The Day of Ripeness:\nFavorable for harming others.",
    "The Day of the Serpent:\nFavorable for requesting wealth.",
    "The Day of Death:\nFavorable for the 'yellow cornfield'.",
    "The Day of The Hunt:\nVery Favorable.",
    "The Day of Seas:\nFavorable for all crops.",
    "The Day of Rain:\nFavorable for recognizing sins.",
    "The Day of The Dog:\nA bad day favored by sorcerers.",
    "The Day of The Artist:\nFavorable to augment all things.",
    "The Day of Mist:\nFavorable for the administration of justice.",
    "The Day of Growth:\nFavorable for blessing children.",
    "The Day of the Underworld:\nFavorable for prayers of multiplication.",
    "The Day of the Moon:\nFavorable for prayers for chickens and benefits.",
    "The Day of Wax:\nFavorable for the 'white cornfield'.",
    "The Day of Honey:\nFavorable for domestic animal prayers.",
    "The Day of Sacrifice:\nFavorable for sacrificing animals.",
    "The Day of Guardians:\nFavorable for compensating for damage."};
  
  //Variable Data ---------------------------------------------------------
  
  private int[] lc,greg,tzolkin,haab,eight,supp;
  private int mdc,crd;
  
  //Constructors -------------------------------------------------------------
  
  public MayanDate(int[] l){
    setLongCount(l);
  }
  
  public MayanDate(int days){
    setMDC(days);
  }
  
  public MayanDate(int d, int m, int y, boolean bc){
    setGregDate(d,m,y,bc);
  }
  
  public MayanDate(int d, int m, int y){
    setGregDate(d,m,y);
  }
  
  public MayanDate(){
   today();
  }
  
  
  //Mutators -------------------------------------------------------------
  
  public void setMDC(int days){
   greg = mdcGREG(days);
   mdc = days;
   lc = longCount(mdc);
   update();
  }
   
  public void setGregDate(int d, int m, int y, boolean bc){
   if(bc){
     y = (0-y)+1;
   }
   greg = new int[3];
   greg[0]=d;
   greg[1]=m;
   greg[2]=y;
   mdc = gregMDC(d,m,y);
   lc = longCount(mdc);
   update();
  }
   
  public void setGregDate(int d, int m, int y){
    greg = new int[3];
    greg[0]=d;
    greg[1]=m;
    greg[2]=y;
    mdc = gregMDC(d,m,y);
    lc = longCount(mdc);
    update();
  }
   
  public void setLongCount(int[] l){
    if(l.length==5){
      lc = l;
    }else{
      int[] zapf = {0,0,0,0,0};
      lc = zapf;
    }
    mdc = unLongCount(lc);
    greg = mdcGREG(mdc);
    update();
  }
   
  public void today(){
    Calendar cal = Calendar.getInstance();
    greg = new int[3];
    greg[0]=cal.get(cal.DATE);
    greg[1]=cal.get(cal.MONTH)+1;
    greg[2]=cal.get(cal.YEAR);
    mdc = gregMDC(cal.get(cal.DATE),cal.get(cal.MONTH)+1,cal.get(cal.YEAR));
    lc = longCount(mdc);
    update();
  }
   
  public void oneUp(){
     setMDC(mdc+1);
  }
   
  public void oneDown(){
     setMDC(mdc-1);
  }
  
  
  //Accessors ---------------------------------------------------------------
  
  public String toString(){
    return lcToString()+"  "+crToString();
  }
  
  public String lcToString(){
    return lcCond(lc);
  }
  
  public String crToString(){
    return crCond(tzolkin,haab);
  }
  
  public String eightToString(){
    String out = "Quadrant: "+EI_QUADRANTS[eight[2]]+" ("+EI_QUADRANTS[eight[2]+4]+")   Color: "+EI_COLORS[eight[2]]+" ("+EI_COLORS[eight[2]+4]+")";
    return out;
  }
  
  public String suppToString(){
    String out = "Lord of the Night: G"+supp[0]+"    7-Cycle: "+supp[3]+"    Year Bearer: "+supp[1]+" "+TZ_NAMES[supp[2]];
    return out;
  }
  
  public String gregToString(){
    return gregCond(greg);
  }
  
  public int[] getTzolkin(){
    return tzolkin;
  }
  
  public int[] getHaab(){
    return haab;
  }
  
  public int[] getEight(){
    return eight;
  }
  
  public int[] getSupp(){
    return supp;
  }
  
  public int[] getLongCount(){
    return lc;
  }
  
  public int[] getGregDate(){
    return greg;
  }
  
  public int getMDC(){
    return mdc;
  }
  
  public int getCRD(){
    return crd;
  }

  
  //Converters -------------------------------------------------------------
  
  public int gregMDC(int day, int month, int year){
    //Gregorian to MDC
    int a = floop((14-month)/12);
    int y = year+4800-a;
    int m = month+(12*a)-3;
    int jday = day+floop(((153*m)+2)/5)+(365*y)+floop(y/4)-floop(y/100)+floop(y/400)-32045;
    return jday-C_CONS;
  }
  
  public int[] mdcGREG(int day){
    //MDC to Gregorian
    int j = day+C_CONS;
    int f = j + 1363 + (((4*j+274277)/146097)*3)/4;
    int e = 4*f+3;
    int g = (e%1461)/4;
    int h = 5*g+2;
    int[] out = new int[3];
    out[0]=((h%153)/5)+1;
    out[1]=(((h/153)+2)%12)+1;
    out[2]=(e/1461)-4716+((14-out[1])/12);
    return out;
  }
  
  public int[] longCount(int day){
    //MDC to [Bak'tun,K'atun,Tun,Winal,K'in]
    int[] out = {0,0,0,0,0};
    //Bak'tun
    out[0]=((day-(day%144000))/144000);
    day = day-(out[0]*144000);
    //K'atun
    out[1]=((day-(day%7200))/7200);
    day = day-(out[1]*7200);
    //Tun
    out[2]=((day-(day%360))/360);
    day = day-(out[2]*360);
    //Winal
    out[3]=((day-(day%20))/20);
    day = day-(out[3]*20);
    //K'in
    out[4]=day;
    return out;
  }
  
  public int unLongCount(int[] l){
    //[Bak'tun,K'atun,Tun,Winal,K'in] to MDC
    return l[4]+(20*l[3])+(360*l[2])+(7200*l[1])+(144000*l[0]);
  }
  
  public int[] tzolkinOf(int day){
    //Tzolk'in from MDC
    int[] out = new int[3];      //[Position, Trecena, Veintena]
    out[0] = (day+159)%260;      //Tzolk'in Position
    out[1] = (out[0]+1)%13;      //Tzolk'in Trecena
    if(out[1]==0)out[1]=13;
    out[2]=Math.abs(out[0]+1)%20;        //Tzolk'in Veintena
    return out;
  }
  
  public int[] haabOf(int day){
    //Haab' from MDC
    int[] out = new int[3];      //[Position, Day, Month]
    out[0] = (day+348)%365;      //Haab' Position
    out[1] = out[0]%20;          //Haab' Day
    out[2] = out[0]/20;          //Haab' Month
    return out;
  }

  public int[] getDistance(int da, int db){
    //Calculates the distance between two given MDCs and returns a standard Long Count.
    return longCount(db-da);
  }
  
  public int[] getDistance(int[] da, int[] db){
    //Calculates the distance between two given Long Counts and returns a standard Long Count.
    return longCount(unLongCount(db)-unLongCount(da));
  }
  
  public int[] giveDistance(int da, int db){
    //Calculates the distance from MDC da given by MDC db and returns the new standard Long Count.
    return longCount(da+db);
  }
  
  public int[] giveDistance(int[] da, int[] db){
    //Calculates the distance from Long Count da given by Distance Number db and returns the new standard Long Count.
    return longCount(unLongCount(da)+unLongCount(db));
  }
  
  public int[] roundBorders(int day){
    //Calculates the Beginning and Ending MDC values of the Calendar Round containing the given MDC.
    int[] out = new int[2];
    out[0]=day-(day%18980);
    out[1]=(day-(day%18980))+18979;
    return out;
  }
  
  public int[] estimate(int rd, int n){
    //Calculates n possible MDC values corresponding to a Calendar Round value, starting at 0.
    if(n<=0)n=1;
    int[] out = new int[n];
    for(int i=0;i<n;i++){
      out[i] = rd+(i*18980);
    }
    return out;
  }
  
  public int[] estimate(int rd, int n, int g){
    //Calculates n possible MDC values corresponding to a Calendar Round value, starting at 0, with a Lord of The Night g.
    if(n<=0)n=1;
    if(g==9)g=0;
    int[] out = new int[n];
    int i=0;
    int z=0;
    while(i<n&&z<100000){
      if((rd+(z*18980))%9==g){
      out[i] = rd+(z*18980);
      i++;
      }
      z++;
    }
    return out;
  }
  
  public int roundPos(int tn, int td, int hn, int hm){
    //Calculates the position in the Calendar Round from Tzolk'in and Haab' values.
    int tpos = (40*( (tn-1) - (td-1) ) + (td-1) );
    tpos = ((tpos % 260) + 260) % 260;
    int hpos = ((hn)+20*(hm));
    hpos = ((hpos % 365) + 365) % 365;
    int numh = (tpos-hpos);
    numh = ((numh % 52) + 52) % 52;
    int pcr = 365*(numh)+hpos;
    pcr = pcr - 7283;
    pcr = ((pcr % 18980) + 18980) % 18980;
    return pcr;
  }
  
  
  //Internal -----------------------------------------------------------------
  
  private void update(){
    //Updates the Calendar Round and Supplementary Series.
    //Calendar Round Position:
    crd = mdc % 18980;
    tzolkin = tzolkinOf(mdc);
    haab = haabOf(mdc);
    
    //819-day Cycle:
    eight = new int[3];                  //[Station Distance, Station Day, Color and Quadrant]
    eight[0] = (mdc+3)%819;              //Distance from last station
    eight[1] = mdc-eight[0];             //MDC of last station 
    eight[2] = tzolkinOf(eight[1])[0]%4; //Color and Quandrant indices of last station
    
    //Supplementary Series:
    supp = new int[4];                   //[Lord of the Night, Year Bearer Number, Year Bearer Name, Seven-Day Cycle]
    supp[0] = mdc%9;                     //Lord of the Night
    if(supp[0]==0)supp[0] = 9;
    int zeroDay = mdc - ((mdc+348)%365);
    supp[1] = tzolkinOf(zeroDay)[1];     //Year Bearer Number
    supp[2] = tzolkinOf(zeroDay)[2];     //Year Bearer Name
    if(mdc<360){
      supp[1] = 8;
      supp[2] = 17;
    }
    supp[3] = eight[0]%7;                //7-day cycle
    if(supp[3]==0)supp[3] = 7;
  }
  
  private int floop(double b){
    //A less messy Math.floor()
    return (int)Math.floor(b);
  }
  
  public String lcCond(int[] lco){
    //Condenses Long Count Dates
    if(lco.length!=5){
      return "ERROR: Not a Long Count";
    }else{
      return lco[0]+"."+lco[1]+"."+lco[2]+"."+lco[3]+"."+lco[4];
    }
  }
  
  public String crCond(int[] tzool,int[] haabf){
    //Condenses Calendar Round Dates
    return tzool[1]+" "+TZ_NAMES[tzool[2]]+" "+haabf[1]+" "+HA_NAMES[haabf[2]];
  }
  
  public String gregCond(int[] gregor){
    //Condenses Gregorian Dates
    String trypp = " CE";
    int yearr = gregor[2];
    if(yearr<=0){
      trypp = " BCE";
      yearr = Math.abs(yearr-1);
    }
    return GR_MONTHS[gregor[1]]+" "+gregor[0]+", "+yearr+trypp;
  }
  


  //Omens -----------------------------------------------------------------

  public String omenYear(int ynum, int yname){
    int power = (int)Math.round(( ((double) ynum) / 13.0 )*100);
    String out = power + "% manifestation of";
    switch(yname){
      case 2:
        //Ik
        out = out + " natural disasters.";
      break;
      case 7:
        //Manik
        out = out + " business loss and illness.";
      break;
      case 12:
        //Eb'
        out = out + " good business and health.";
      break;
      case 17:
        //Kaban
        out = out + " creativity and ideas.";
      break;
      default:
        out = out + " -- ERROR!";
      break;
    }
    return out;
  }
  
  public String omenDay(int tzolname){
    return DAY_PROPHECIES[tzolname];
  }

}







