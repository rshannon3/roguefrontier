package mapUnit;

import game.MapUnitList;

import java.awt.event.MouseEvent;

import java.awt.geom.*;
import java.awt.geom.Point2D.Double;

import detection.DetectionCircle;

public class Actor extends MapUnit
{
    
    public static final double DEFAULT_SPEED = 2.0;
    private double currentMoveSpeed = DEFAULT_SPEED; //TODO cannot get within movespeed number of pixels of any object
    private double baseMoveSpeed = DEFAULT_SPEED;
    
    //---------------------------------------------------ACTOR STATES ---------------------------------------------//
    private boolean moving = false; 
    private boolean turning = false; // is the actor turning
    //------------------------------------------------------------------------------------------------------------//
    
    private Point2D.Double target; // The target point of the current move
    private double moveAngle = 0;  // the angle at which the actor is moving
    private double facingAngle = 0; // the angle the actor is turning towards
    private double targetAngle = 0;
    private double turnSpeed = Math.PI/270.0; // The max number of radians the character can turn per tick
    
    public Actor(int x, int y, MapUnitList obs)
    {
    	super(obs, new DetectionCircle(x, y, MapUnit.DEFAULT_SIZE));
    }
    
    public void act()
    {
    	if(colliding())
    	{
    		System.err.println("started act colliding");
    	}
    	if(turning)
    		turn();
    	if(moving)
    		move();
    }
    
    
    public void turn()
    {
    	// Updates the move angle to the target angle. 
    	// The getTargetAngle() function allows for how the target angle is calculated to be changed
    	facingAngle = getTargetAngle();
    	turning = false;
    }
    
    public void turnWithTurnspeed()
	{
    	boolean counterClockwise = false;
		if(targetAngle > facingAngle)
			counterClockwise = true;
		if(Math.abs(targetAngle-facingAngle) > Math.PI)
			counterClockwise = !counterClockwise;
		
		if(counterClockwise)
		{
			//CounterClockwise
			facingAngle = facingAngle + Math.min(turnSpeed, Math.abs(targetAngle-facingAngle));
		}
		else	//Clockwise
		{
			facingAngle = facingAngle - Math.min(turnSpeed, Math.abs(targetAngle-facingAngle));
		}
		if(facingAngle < Math.PI)
			facingAngle += Math.PI*2.0;
		if(facingAngle > Math.PI)
			facingAngle -= Math.PI*2.0;
		
	}
    
    /*
    public void act()
    {
    	if(!moving && !keyMoving && !turning)
    		return;
    	if(colliding())
    	{
    		System.out.println("started move colliding");
    		moving = false;
    	}
    	if(turning)
    	{
    		boolean counterClockwise = false;
    		if(targetAngle > moveAngle)
    			counterClockwise = true;
    		if(Math.abs(targetAngle-moveAngle) > Math.PI)
    			counterClockwise = !counterClockwise;
    		
    		if(counterClockwise)
    		{
    			//CounterClockwise
    			turnTo( moveAngle + Math.min(turnSpeed, Math.abs(targetAngle-moveAngle)) );
    		}
    		else	//Clockwise
    		{
    			turnTo( moveAngle - Math.min(turnSpeed, Math.abs(targetAngle-moveAngle)) );
    		}
    		if(moveAngle < Math.PI)
    			moveAngle += Math.PI*2.0;
    		if(moveAngle > Math.PI)
    			moveAngle -= Math.PI*2.0;
    		
    		//turnFrame++;
    		//turnFrame %= turnSpeed;
    		//if(turnFrame == 0)
    			//turning = false;
    			
    		//System.out.println("MoveAngle: "+moveAngle+"    TargetAngle: "+targetAngle);
    		if(Math.abs(moveAngle - targetAngle) < turnSpeed)
    		{
    			moveAngle = targetAngle;
    			turning = false;
    		}
    	}
    	else if( moving || keyMoving ) // removed mouse input
        {
        	Point2D.Double myOldPoint = (Point2D.Double)collisionBox.getLocation().clone();
            move(moveAngle);
            if(colliding())
            {
            	System.out.println("Poppin back");
            	collisionBox.getLocation() = (Point2D.Double)myOldPoint.clone();
            	collisionBox.setLocation(collisionBox.getLocation().x, collisionBox.getLocation().y);
            	if(moving);
            		//edgewalk();
        	}
            //If each dimension of currentLocation is within half a pixel of target location,
            else if( target != null && Math.abs(collisionBox.getLocation().x - target.x) <= (0.5*currentMoveSpeed) && Math.abs(collisionBox.getLocation().y - target.y) <= (0.5*currentMoveSpeed) )
            {
            	//Then stop moving and set location to target
                moving = false;
                //collisionBox.getLocation().x = target.x;
                //collisionBox.getLocation().y = target.y;
                collisionBox.setLocation((int)target.x, (int)target.y);
                System.out.println("No longer moving.");
            	System.out.println("Loc: " + collisionBox.getLocation().x + "," + collisionBox.getLocation().y + "    Target: " + target.x + "," + target.y);
            }
        }
    }   */

	protected void move()
    {
    	if(canMove())
    	{
    		move(moveAngle);
    	}else if(!edgeWalk()){
    		//pathFind();
    	}
    }
    
	//For now just try moving horizontally
    private void pathFind() {
    	double left = moveAngle-Math.PI;
    	double right = moveAngle+Math.PI;
    	if(canMove(left)){
			move(left);
		}else if(canMove(right)){
			move(right);
		}
	}

	public boolean canMove()
    {
    	return canMove(moveAngle);
    }
    
    public boolean canMove(double movAngl)
    {
    	double x = collisionBox.getLocation().getX();
    	double y = collisionBox.getLocation().getY();
        move(movAngl);
        boolean result = this.getCollisionBox().isTraversable()|| !colliding();// Allows player to move freely if traversable
        collisionBox.setLocation(x, y);
        return result;	
    }
    

	private boolean edgeWalk() {
		
		if(this.moveAngle % (Math.PI/2) == 0){
			return false;
		}
		double firstEdgeAngle = this.getClosestAngle(moveAngle, 45);
		
		double secondEdgeAngle;
		if(firstEdgeAngle < moveAngle){
			secondEdgeAngle= standardizeAngle(firstEdgeAngle+Math.toRadians(45));
			if(firstEdgeAngle == moveAngle){
				firstEdgeAngle = standardizeAngle(firstEdgeAngle - Math.toRadians(45));
			}
		}else{
			secondEdgeAngle= standardizeAngle(firstEdgeAngle-Math.toRadians(45));
			if(firstEdgeAngle == moveAngle){
				firstEdgeAngle = standardizeAngle(firstEdgeAngle + Math.toRadians(45));
			}
		}
		/*System.out.println("moveAngle:" + Math.toDegrees(moveAngle));
		System.out.println("firstAngle:" + Math.toDegrees(firstEdgeAngle));
		System.out.println("secondAngle:" + Math.toDegrees(secondEdgeAngle));
		*/
		if(canMove(firstEdgeAngle)){
			move(firstEdgeAngle);
			return true;
		}else if(canMove(secondEdgeAngle)){
			move(secondEdgeAngle);
			return true;
		}
		return false;
    }
	
	private double getClosestAngle(double currAngle, int closestAngleIncrementDeg){
		double smallerEdgeAngle = Math.toRadians((int)(Math.toDegrees(moveAngle))/closestAngleIncrementDeg*closestAngleIncrementDeg);
		if(standardizeAngle(smallerEdgeAngle-currAngle) < Math.toRadians(closestAngleIncrementDeg/2.0)){
			return smallerEdgeAngle;
		}else{
			return standardizeAngle(smallerEdgeAngle-Math.toRadians(closestAngleIncrementDeg));
		}
	}

	private double standardizeAngle(double result){
		if(result > Math.PI){
			return result-Math.PI;
		}else if(result < 0-Math.PI){
			return result+Math.PI;
		}else{
			return result;
		}
	}
	/*
    private void edgewalkHelper(boolean secondPass)
    {
    	Point2D.Double myOldPoint = (Point2D.Double)collisionBox.getLocation().clone();
    	if(moveAngle < 0) moveAngle += Math.PI*2;
    	double edgeAngle = Math.toRadians((int)(Math.toDegrees(moveAngle))/90*90);
    	System.out.println("Edge Angles: " + Math.toDegrees(edgeAngle)+" & "+Math.toDegrees(edgeAngle+Math.toRadians(90.0))   );
    	move(edgeAngle);
        if(colliding())
        {
        	collisionBox.getLocation() = (Point2D.Double) myOldPoint.clone();
        	collisionBox.setLocation((int)collisionBox.getLocation().x, (int)collisionBox.getLocation().y);
		    move(edgeAngle+Math.toRadians(90.0));
		    if(colliding())
		    {
		    	collisionBox.getLocation() = (Point2D.Double) myOldPoint.clone();
		    	collisionBox.setLocation((int)collisionBox.getLocation().x, (int)collisionBox.getLocation().y);
		    	if(!secondPass)
		    	{
		    		double retreatAngle = moveAngle + Math.toRadians(135.0);
		    		collisionBox.getLocation().x += this.roundAwayFromZero(Math.cos(retreatAngle));
		    		//System.out.println("moving x:" + this.roundAwayFromZero(Math.cos(retreatAngle)));
		            collisionBox.getLocation().y -= this.roundAwayFromZero(Math.sin(retreatAngle));
		            //System.out.println("moving y:" + this.roundAwayFromZero(Math.sin(retreatAngle)));
		            collisionBox.setLocation((int)collisionBox.getLocation().x, (int)collisionBox.getLocation().y);
		            moveAngle = Math.atan2((collisionBox.getLocation().y - target.y), target.x - collisionBox.getLocation().x);
		    		edgewalkHelper(true);
		    	}
		    	//else
		    		//moving = false;
			}
        }
        moveAngle = Math.atan2((collisionBox.getLocation().y - target.y), target.x - collisionBox.getLocation().x);
		return;
		
	}*/
	/** Iterates over obstructions and checks if they are colliding with this Actor.
    * If so, moves the Actor just outside of the object, back along the same angle from which it entered during the move
    */
    
    private boolean colliding()
	{
    	for(MapUnit shape : obs.getObstacleList())
    	{
    		if(collisionBox.collision(shape.getCollisionBox()) && !(shape.getCollisionBox().equals(this.collisionBox)) && !shape.getCollisionBox().isTraversable() )
    		{
    			//System.out.println("Collision detected!");
    			collidedWith(shape);
    			return true;
    		}
    	}
    	return false;
	}
            
    /*
	public void moveToward(int destX, int destY)
    {
		if(destX == collisionBox.getLocation().x && destY == collisionBox.getLocation().y)
		{
			System.out.println("Atempted to move to current location.  Ignoring move.");
			moving = false;
			return;
		}
        target = new Point2D.Double(destX, destY);
        targetAngle = Math.atan2((collisionBox.getLocation().y - destY), destX - collisionBox.getLocation().x);
        if(Math.abs(targetAngle - moveAngle) > turnSpeed)
        	turning = true;
        else
        	moveAngle = targetAngle;
        moving = true;
        //System.out.println("NOW MOVING!");
        //System.out.println("My move angle is " + Math.toDegrees(moveAngle)/* + (moveAngle * 180) / Math.PI)*///);
        //System.out.println("My target point is x: " + destX + " y: " + destY);
    //}*/
	
	/* TODO DEBUG THESE TURN METHODS
	 * NO CURRENT REFERENCES
	 * Turns to face a given point
	 */
    /*
	public void turnTo(int destX, int destY)
	{
		target = new Point2D.Double(destX, destY);
        moveAngle = Math.atan2((collisionBox.getLocation().y - destY), destX - collisionBox.getLocation().x);
	}
	
	/* NO CURRENT REFERENCES
	public void turnToward(double t)
	{
		if(Math.abs(t-moveAngle) < turnSpeed)
		{
			moveAngle = t;
			return;
		}
		turning = true;
	}*/
	
    /**
     * Called when this Actor collides with a MapUnit
     * Override to do something when an actor touches something
     * @param shape - The MapUnit this object collided with
     */
	protected void collidedWith(MapUnit shape)
	{
		return;
		
	}

	public void startTurn(int destX, int destY)
	{
		target = new Point2D.Double(destX, destY);
        targetAngle = Math.atan2((collisionBox.getLocation().y - destY), destX - collisionBox.getLocation().x);
        turning = true;
	}
	
	public void startTurn(double ang)
	{
        targetAngle = ang;
        turning = true;
	}
	
	private double roundAwayFromZero(double d)
	{
		if(d<0)	return Math.floor(d);
		return Math.ceil(d);
	}
    
    public void move(double moveAngle)
    {
        
        double moveX = Math.cos(moveAngle) ;
        double moveY = Math.sin(moveAngle) ;
        
        //Moves according to your angle and movespeed
        collisionBox.getLocation().x = collisionBox.getLocation().x + moveX * currentMoveSpeed;
        collisionBox.getLocation().y = collisionBox.getLocation().y - moveY * currentMoveSpeed;
        collisionBox.setLocation(collisionBox.getLocation().x, collisionBox.getLocation().y);
        
    }
    
    
    public boolean isMoving()
    { return moving; }
    
    public void setMoving(boolean b)
    { moving = b; } 
    
    public void toggleMoving()
    { moving = !moving; }
    
    public double getMoveSpeed()
    { return currentMoveSpeed; }
    
    public double getMoveAngle()
    { return moveAngle; }
    
    public void setMoveAngle(double m)
    { moveAngle = m; }
    
    public void setMoveAngle(double destX, double destY)
    {
    	moveAngle = Math.atan2((collisionBox.getLocation().y - destY), destX - collisionBox.getLocation().x);
    }
    
    public void setFacingAngle(double ang)
    { facingAngle = ang; }
    
    public double getFacingAngle()
    { return facingAngle; }
	
	public boolean isTurning()
	{return turning;}
	
	public void stopMoving()
	{
		moving = false;
	}
	
	public void setMoveSpeed(double s)
	{ currentMoveSpeed = s; }
	
	public double getTurnSpeed()
	{
		return turnSpeed;
	}

	public void setTurnSpeed(double turnSpeed)
	{
		this.turnSpeed = turnSpeed;
	}
	
	public MapUnitList getObs()
	{ return obs; }
	
    private double getTargetAngle()
    {
    	// returns the target angle
    	// Should be overwritten in subclasses
		return targetAngle;
	}
        
}
