import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] nodes;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, endNode, currentNode;
    boolean goalreached=false;
    int step=0;

    public PathFinder(GamePanel gp){
        this.gp=gp;
        initializeNodes();
    }
    public void initializeNodes(){
        nodes = new Node[gp.MAP_HEIGHT][gp.MAP_WIDTH];
        int col=0;
        int row=0;

        while(col<gp.MAP_HEIGHT&&row<gp.MAP_WIDTH){
            while(row<gp.MAP_WIDTH){
                nodes[col][row] = new Node(col, row);
                row++;
            }
            if(row == gp.MAP_WIDTH){
                row=0;
                col++;
            }
        }
    }
    public void resetNodes(){
        int col=0;
        int row=0;
        while(col<gp.MAP_HEIGHT&&row<gp.MAP_WIDTH){
            nodes[col][row].open=false;
            nodes[col][row].checked=false;
            nodes[col][row].solid=false;
            col++;
            if(col==gp.MAP_WIDTH){
                col=0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalreached=false;
        step=0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow){

        resetNodes();
        startNode=nodes[startCol][startRow];
        currentNode=startNode;
        endNode=nodes[goalCol][goalRow];
        openList.add(currentNode);

        int col=0;
        int row=0;

        while(col<gp.MAP_HEIGHT&&row<gp.MAP_WIDTH){

            int tileNum = gp.tileM.mapTileNum[row][col];

            if(gp.tileM.tile[tileNum].collision){
                nodes[col][row].solid=true;
            }
            //NEED TO CHECK DESTRUCTIBLE TILES

            //COST
            getCost(nodes[col][row]);

            col++;
            if(col==gp.MAP_WIDTH){
                col=0;
                row++;
            }
        }
        
    }
    public void getCost(Node node){

        //G cost
        int xDistance = Math.abs(node.col-startNode.col);
        int yDistance = Math.abs(node.row-startNode.row);
        node.gCost=xDistance+yDistance;
        //H cost
        xDistance = Math.abs(node.col-endNode.col);
        yDistance = Math.abs(node.row-endNode.row);
        node.hCost=xDistance+yDistance;
        //F cost
        node.fCost=node.hCost+node.gCost;
        
    }
    public boolean search(){
        while(!goalreached&&step<1000){
            int col=currentNode.col;
            int row=currentNode.row;

            //check current node
            currentNode.checked=true;
            openList.remove(currentNode);

            //check up node
            if(row-1>=0){
                openNode(nodes[col][row-1]);
            }
            //check down node
            if(row+1>=0&&row+1<gp.MAP_WIDTH){
                openNode(nodes[col][row+1]);
            }
            //check right node
            if(col+1>=0&&col+1<gp.MAP_HEIGHT){
                openNode(nodes[col+1][row]);
            }
            //check left node
            if(col-1>=0){
                openNode(nodes[col-1][row]);
            }

            //find best node
            int bestNodeIndex=0;
            int bestNodefCost=999;
            for(int i=0;i<openList.size();i++){

                //compare fCost
                if(openList.get(i).fCost<bestNodefCost){
                    bestNodeIndex=i;
                    bestNodefCost=openList.get(i).fCost;
                }
                else if(openList.get(i).fCost==bestNodefCost){
                    if(openList.get(i).gCost<openList.get(bestNodeIndex).gCost){
                        bestNodeIndex=i;
                    }

                }
                //end loop if there is no nodes in openlist
                if(openList.size()==0){
                    break;
                }
                currentNode=openList.get(bestNodeIndex);

                if(currentNode==endNode){
                    goalreached=true;
                    trackPath();
                }
            }
            step++;
        }
        return goalreached;
    }
    public void openNode(Node node){
        if(node.open==false&&node.checked==false&&node.solid==false){

            node.open=true;
            node.parent=currentNode;
            openList.add(node);
        }
    }
    public void trackPath(){
        Node current = endNode;
        while(current!=startNode){
            pathList.add(0,current);
            current=current.parent;
        }
    }
}