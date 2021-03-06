package cn.zy.offer;

import jdk.nashorn.internal.ir.BinaryNode;

/**
 * @Title: 平衡二叉树 AVL
 * 定义: 节点的左右子树高度差不超过 1 （小于等于1）
 * 难点：树的旋转 LL RR LR RL
 * LL : 左左单旋转 （向右旋转） 情景 新插入节点是左子树的左叶子节点 导致高度差出现问题
 * 注意点 旋转的 点的成为新父节点的右子树 原来点的右子树 成为旋转点的左子树
 *
 * RR : 右右单旋转 （向左旋转） 情景 新插入节点是右子树的右叶子节点 导致高度差出现问题
 *
 * LR : 左右单旋转 （先左后右旋转） 情景 新插入节点是左子树的右叶子节点 导致高度差出现问题，解决方案
 * 先将其左子树 与新插入的 节点进行左旋转 然后再将父节点与新的左子树进行右旋
 *
 * RL : 右左单旋转 （先右后左旋转） 情景 新插入节点是右子树的左叶子节点 导致高度差出现问题
 * 先将其右子树 与新插入的 节点进行右旋转 然后再将父节点与新的右子树进行左旋

 *
 *
 * @Project:
 * @Author: zy
 * @Description:
 * @Date: Create in 21:51 2019/3/19
 */
public class AVLTree <T extends Comparable> {


    public AVLNode<T> root;


    class AVLNode<T extends Comparable> {

        public AVLNode<T> left;

        public AVLNode<T> right;

        public T data;

        public int height;//当前结点的高度

        public AVLNode(T data) {
            this(null,null,data);
        }

        public AVLNode(AVLNode<T> left, AVLNode<T> right, T data) {
            this(left,right,data,0);
        }

        public AVLNode(AVLNode<T> left, AVLNode<T> right, T data, int height) {
            this.left=left;
            this.right=right;
            this.data=data;
            this.height = height;
        }

    }


    int height(AVLNode<T> root) {
        if(root == null) {
            return 0;
        } else {
            return Math.max(height(root.left),height(root.right)) + 1;
        }
    }

    /**
     * 左左单旋转(LL旋转) w变为x的根结点, x变为w的右子树
     *
     * LL
     * @param x
     * @return
     */
    private AVLNode<T> singleRotateLeft(AVLNode<T> x){
        //把w结点旋转为根结点
        AVLNode<T> w=  x.left;
        //同时w的右子树变为x的左子树
        x.left=w.right;
        //x变为w的右子树
        w.right=x;
        //重新计算x/w的高度
        x.height=Math.max(height(x.left),height(x.right))+1;
        w.height=Math.max(height(w.left),x.height)+1;
        return w;//返回新的根结点
    }

    /**
     * RR
     * 右右单旋转(RR旋转) x变为w的根结点, w变为x的左子树
     * @return
     */
    private AVLNode<T> singleRotateRight(AVLNode<T> w){

        AVLNode<T> x=w.right;

        w.right=x.left;
        x.left=w;

        //重新计算x/w的高度
        w.height=Math.max(height(w.left),height(w.right))+1;
        x.height=Math.max(height(x.left),w.height)+1;

        //返回新的根结点
        return x;
    }


    /**
     * 左右旋转(LR旋转) x(根) w y 结点 把y变成根结点
     * @return
     */
    private AVLNode<T> doubleRotateWithLeft(AVLNode<T> x){
        //w先进行RR旋转
        x.left=singleRotateRight(x.left);
        //再进行x的LL旋转
        return singleRotateLeft(x);
    }

    /**
     * 右左旋转(RL旋转)
     * @param w
     * @return
     */
    private AVLNode<T> doubleRotateWithRight(AVLNode<T> x){
        //先进行LL旋转
        x.right=singleRotateLeft(x.right);
        //再进行RR旋转
        return singleRotateRight(x);
    }


    /**
     * 插入方法
     * @param data
     */
    public void insert(T data) {
        if (data==null){
            throw new RuntimeException("data can\'t not be null ");
        }
        this.root=insert(data,root);
    }

    private AVLNode<T> insert(T data , AVLNode<T> p){

        //说明已没有孩子结点,可以创建新结点插入了.
        if(p==null){
            p=new AVLNode<T>(data);
        }else if(data.compareTo(p.data)<0){//向左子树寻找插入位置
            p.left=insert(data,p.left);

            //插入后计算子树的高度,等于2则需要重新恢复平衡,由于是左边插入,左子树的高度肯定大于等于右子树的高度
            if(height(p.left)-height(p.right)==2){
                //判断data是插入点的左孩子还是右孩子
                if(data.compareTo(p.left.data)<0){
                    //进行LL旋转
                    p=singleRotateLeft(p);
                }else {
                    //进行左右旋转
                    p=doubleRotateWithLeft(p);
                }
            }
        }else if (data.compareTo(p.data)>0){//向右子树寻找插入位置
            p.right=insert(data,p.right);

            if(height(p.right)-height(p.left)==2){
                if (data.compareTo(p.right.data)<0){
                    //进行右左旋转
                    p=doubleRotateWithRight(p);
                }else {
                    p=singleRotateRight(p);
                }
            }
        }
        else
            ;//if exist do nothing
        //重新计算各个结点的高度
        p.height = Math.max( height( p.left ), height( p.right ) ) + 1;

        return p;//返回根结点
    }

    /**
    **
    ** 删除方法
    * @param data
    */
    public void remove(T data) {
        if (data==null){
            throw new RuntimeException("data can\'t not be null ");
        }
        this.root=remove(data,root);
    }

    /**
     * 删除操作
     *
     * 删除操作比较复杂，这里只说一下原理，不写代码了。

     1）如果待删除的结点是叶子结点，那么直接删除该叶子结点，并将其父结点对应指针置nullptr，同时，从下向上依次调整该条路径上的平衡。这是最简单的情况

     2）当待删除的结点只有一棵子树的时候，将待删除结点的父结点对应的指针指向该子树，之后，删除该结点，同时，从该父结点开始，从下向上依次调整该条路径上的平衡。

     3）当待删除的结点有左右两棵子树时，找到该结点的相邻关键字。然后，将该关键字的值赋值给待删除的结点。这样，就把问题转化为删除该关键字所在的叶子结点问题了。即第1）种情况。

     解释一下什么叫做相邻关键字。对于不在叶子结点上的关键字a，它的相邻关键字为其左子树中的最大值或
     其右子树中的最小值。从该定义可以清晰地看出，相邻关键字一定位于叶子结点处。更直白地说，
     相邻关键字就是，沿着a的左指针来到其左子树的根结点，然后沿着右指针一下往右走，直到叶子结点为止。
     或者是，  沿着a的右指针来到其右子树的根结点，然后沿着左指针一下往左走，直到叶子结点为止。

     *
     * @param data
     * @param p
     * @return
     */
    private AVLNode<T> remove(T data,AVLNode<T> p){

        if(p ==null)
            return null;

        int result=data.compareTo(p.data);

        //从左子树查找需要删除的元素
        if(result<0){
            p.left=remove(data,p.left);

            //检测是否平衡
            if(height(p.right)-height(p.left)==2){
                AVLNode<T> currentNode=p.right;
                //判断需要那种旋转
                if(height(currentNode.left)>height(currentNode.right)){
                    //LL
                    p=singleRotateLeft(p);
                }else{
                    //LR
                    p=doubleRotateWithLeft(p);
                }
            }

        }
        //从右子树查找需要删除的元素
        else if(result>0){
            p.right=remove(data,p.right);
            //检测是否平衡
            if(height(p.left)-height(p.right)==2){
                AVLNode<T> currentNode=p.left;
                //判断需要那种旋转
                if(height(currentNode.right)>height(currentNode.left)){
                    //RR
                    p=singleRotateRight(p);
                }else{
                    //RL
                    p=doubleRotateWithRight(p);
                }
            }
        }
        //已找到需要删除的元素,并且要删除的结点拥有两个子节点
        else if(p.right!=null&&p.left!=null){

            //寻找替换结点 查找左树最大的子节点 以及右树最小的节点
            p.data=findMin(p.right).data;

            //移除用于替换的结点
            p.right = remove( p.data, p.right );
        }
        else {
            //只有一个孩子结点或者只是叶子结点的情况
            p=(p.left!=null)? p.left:p.right;
        }

        //更新高度值
        if(p!=null)
            p.height = Math.max( height( p.left ), height( p.right ) ) + 1;
        return p;
    }


    /**
     * 查找最小值结点
     * @param p
     * @return
     */
    private AVLNode<T> findMin(AVLNode<T> p){
        if (p==null)//结束条件
            return null;
        else if (p.left==null)//如果没有左结点,那么t就是最小的
            return p;
        return findMin(p.left);
    }

    public T findMin() {
        return findMin(root).data;
    }

    public T findMax() {
        return findMax(root).data;
    }

    /**
     * 查找最大值结点
     * @param p
     * @return
     */
    private AVLNode<T> findMax(AVLNode<T> p){
        if (p==null)
            return null;
        else if (p.right==null)//如果没有右结点,那么t就是最大的
            return p;
        return findMax(p.right);
    }

    public BinaryNode findNode(T data) {
        /**
         * @see BinarySearchTree#findNode(Comparable)
         * @return
         */
        return null;
    }

    private void printTree( AVLNode<T> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.data );
            printTree( t.right );
        }
    }

    public boolean contains(T data) {
        return data != null && contain(data, root);
    }

    public boolean contain(T data , AVLNode<T> subtree){

        if (subtree==null)
            return false;

        int result =data.compareTo(subtree.data);

        if (result<0){
            return contain(data,subtree.left);
        }else if(result>0){
            return contain(data,subtree.right);
        }else {
            return true;
        }
    }

    public void clear() {
        this.root=null;
    }


    public String preOrder() {
        String sb=preOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 先根遍历
     * @param subtree
     * @return
     */
    public String preOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null) {
            //先访问根结点
            sb.append(subtree.data).append(",");
            //访问左子树
            sb.append(preOrder(subtree.left));
            //访问右子树
            sb.append(preOrder(subtree.right));
        }
        return sb.toString();
    }

    public String inOrder() {
        String sb=inOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 中根遍历
     * @param subtree
     * @return
     */
    private String inOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null) {
            //访问左子树
            sb.append(inOrder(subtree.left));
            //访问根结点
            sb.append(subtree.data).append(",");
            //访问右子树
            sb.append(inOrder(subtree.right));
        }
        return sb.toString();
    }

    public String postOrder() {
        String sb=postOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 后根遍历
     * @param subtree
     * @return
     */
    private String postOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null){
            //访问左子树
            sb.append(postOrder(subtree.left));
            //访问右子树
            sb.append(postOrder(subtree.right));
            //访问根结点
            sb.append(subtree.data).append(",");
        }
        return sb.toString();
    }

    public String levelOrder() {
        /**
         * @see BinarySearchTree#levelOrder()
         * @return
         */
        return null;
    }



    /**
     * 测试
     * @param arg
     */
    public  static void main(String arg[]){

        AVLTree<Integer> avlTree=new AVLTree<>();

        for (int i = 1; i <18 ; i++) {
            avlTree.insert(i);
        }

        avlTree.printTree(avlTree.root);
       /* //删除11,8以触发旋转平衡操作
        avlTree.remove(11);
        avlTree.remove(8);
*/
        System.out.println("================");

        avlTree.printTree(avlTree.root);

        System.out.println("findMin:"+avlTree.findMin());

        System.out.println("findMax:"+avlTree.findMax());

        System.out.println("15 exist or not : " + avlTree.contains(15) );

        System.out.println("先根遍历:"+avlTree.preOrder());

        System.out.println("中根遍历:"+avlTree.inOrder());

        System.out.println("后根遍历:"+avlTree.postOrder());

    }


}
