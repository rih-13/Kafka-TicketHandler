package com.cs4411;

public class Ticket {
    public final Long ticketID;
    private boolean sold;
    private long userID;
    public Ticket(Long tid){
        this.ticketID = tid;
        this.sold = false;
        this.userID = -255L;
    }

    public Long getUser(){
        return this.userID;
    }
    public boolean isSold(){
        return this.sold;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Ticket)){
            return false;
        }
        else{
            if (((Ticket) o).ticketID.equals(this.ticketID)){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
