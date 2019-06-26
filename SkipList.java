// Dariel Tenf
// da287484
// COP 3503, Spring 2019

import java.io.*;
import java.util.*;
import java.lang.Math;

class Node<T extends Comparable<T>>
{
	T data;
	ArrayList<Node<T>> Nheight;

	// Initializes Nheight to an ArrayList of size height with all elements set to null
	Node(int height)
	{
		this.Nheight = new ArrayList<Node<T>>(height);
		for (int z = 0; z < height; z++)
			this.Nheight.add(null);
	}
	// Initializes Nheight to an ArrayList of size height with all elements set to null
	// Sets the data field equal to data
	Node(T data, int height)
	{
		this.data = data;
		this.Nheight = new ArrayList<Node<T>>(height);
		for (int z = 0; z < height; z++)
			this.Nheight.add(null);
	}
	public T value()
	{
		return this.data;
	}
	public int height()
	{
		return  this.Nheight.size();
	}
	public Node<T> next(int level)
	{
		if (level < 0 || level > (height() - 1))
			return null;
		return this.Nheight.get(level);
	}
	// Adds a null reference at the end of the ArrayList, incrementing
	// the height by one
	public void grow()
	{
		this.Nheight.add(null);
	}
	// 50/50 chance for the node that's passed to grow in height
	public boolean maybeGrow()
	{
		if ((int)(Math.random()*2) == 1)
		{
			this.Nheight.add(null);
			return true;
		}
		return false;
	}
	// Removes uppermost level from Nheight
	public void trim(int height)
	{
		while (this.height() > height)
		{
			Nheight.remove(height() - 1);
		}
	}
}

public class SkipList<T extends Comparable<T>>
{
	Node<T> head;
	int n;
	// Initializes SkipList by initializing the head node and setting n = 0
	SkipList()
	{
		n = 0;
		this.head = new Node<T>(1);
	}
	SkipList(int height)
	{
		n = 0;
		if (height < 1)
			this.head = new Node<T>(1);
		else
		{
			this.head = new Node<T>(height);
		}
	}
	public int size()
	{
		return n;
	}
	public int height()
	{
		return head.height();
	}
	public Node<T> head()
	{
		return head;
	}
	public void insert(T data)
	{
		Node<T> trav = head, trav2;
		int h;
		int icheck = 0;
		int a = 1;
		int rheight = generateRandomHeight(getMaxHeight(n));
		int r = rheight - 1;
		ArrayList<Node<T>> relink = new ArrayList<Node<T>>();
		// Case for inserting the first node in the SkipList
		if (n == 0)
		{
			head.Nheight.set(0, new Node<T>(data, rheight));
			n++;
			// Sets all relevant references in the head node equal to the new node
			// based on the new node's height
			while (r != 0)
			{
				head.Nheight.set(r, head.next(0));
				r--;
			}
		}
		else
		{
			h = trav.height() - 1;
			// icheck is initialized to 0, so when it equals 1, the method will return

			while (icheck != 1 && trav != null)
			{
				// Adds the node when a drop occurs to relink, which will maintain
				// a trail for when the nodes have to be relinked after the insertion
				boolean c = (trav.next(h) == null || trav.next(h).value().compareTo(data) >= 0);
				while (c && h > 0)
				{
					relink.add(trav);
					h--;
				}
				if (h == 0)
				{
					// If the next node on level zero in null or the value is
					// greater than the value being inserted, trav is at the
					// location to insert
					if (trav.next(h) == null || trav.next(h).value().compareTo(data) >= 0)
					{
						Node<T> iNode = new Node<T>(data, rheight);
						iNode.Nheight.set(0, trav.next(h));
						trav.Nheight.set(h, iNode);
						// This assignment will prompt the program to exit the loop
						// after the insertion occurs
						icheck = 1;
						n++;
						// Relinks the trail left by looking for where to insert
						Collections.reverse(relink);
						for (Node<T> link : relink)
						{
							if (a > rheight - 1)
							{
								continue;
							}
							iNode.Nheight.set(a, link.next(a));
							link.Nheight.set(a, iNode);
							a++;
						}
						// Checks if the insertion prompted an increase in height
						if (getMaxHeight(n) > height())
						{
							head.grow();
							trav2 = head;
							int ptl = head.height() - 2;
							// tallboi maintains the node(s) that have grown to link the
							// references on the top level, while trav2 searches for
							// potential nodes that can grow
							Node<T> tallboi = trav2;
							while (trav2 != null)
							{
								if (trav2.next(ptl) == null)
								{
									trav2 = trav2.next(ptl);
								}
								else
								{
									boolean maybe = trav2.next(ptl).maybeGrow();
									if (maybe)
									{
										tallboi.Nheight.set(ptl + 1, trav2.next(ptl));
										trav2 = trav2.next(ptl);
										tallboi = trav2;
									}
									else
									{
										trav2 = trav2.next(ptl);
									}
								}
							}
						}
					}
					else
					{
						trav = trav.next(h);
					}
				}
				else
				{
					trav = trav.next(h);
				}
			}
		}
		return;
	}
	
	// returns either log base 2 of n or the height that the SkipList
	// was initialized to, whichever is greater
	private int getMaxHeight(int n)
	{
		if (n == 0)
			return height();
		return Math.max((int)(Math.ceil(Math.log(n) / Math.log(2))), height());
	}

	// returns 1 or log base 2 of n, whichever is greater
	private int getMaxHeightdel(int n)
	{
		if(n == 0 || n == 1)
			return 1;
		return (int)(Math.ceil(Math.log(n) / Math.log(2)));
	}

	// Starts with height equal to 1. If the RNG statement returns 0 the function
	// returns height. Otherwise, height increments by 1 and the process continues
	// until either maxHeight is reached or the RNG yields 0
	private static int generateRandomHeight(int maxHeight)
	{
		int height = 1;
		while ((int)(Math.random() * 2) > 0 && height < maxHeight)
		{
			height++;
		}
		return height;
	}

	// Performs the same operations as insert(T data), but height is
	// not randomly generated
	public void insert(T data, int height)
	{
		while (head.height() < height)
		{
			head.grow();
		}
		Node<T> trav = head, trav2;
		int h;
		int icheck = 0;
		int a = 1;
		int r = height - 1;
		ArrayList<Node<T>> relink = new ArrayList<Node<T>>();
		if (n == 0)
		{
			head.Nheight.set(0, new Node<T>(data, height));
			n++;
			while (r != 0)
			{
				head.Nheight.set(r, head.next(0));
				r--;
			}
		}
		else
		{
			h = trav.height() - 1;
			while (icheck != 1)
			{
				while ((trav.next(h) == null || trav.next(h).value().compareTo(data) >= 0) && h > 0)
				{
					relink.add(trav);
					h--;
				}
				if (h == 0)
				{
					if (trav.next(h) == null || trav.next(h).value().compareTo(data) >= 0)
					{
						Node<T> iNode = new Node<T>(data, height);
						iNode.Nheight.set(0, trav.next(h));
						trav.Nheight.set(h, iNode);
						icheck = 1;
						n++;
						Collections.reverse(relink);
						for (Node<T> link : relink)
						{
							if (a > height - 1)
							{
								continue;
							}
							iNode.Nheight.set(a, link.next(a));
							link.Nheight.set(a, iNode);
							a++;
						}
						if (getMaxHeight(n) > height())
						{
							head.grow();
							trav2 = head;
							int ptl = head.height() - 2;
							Node<T> tallboi = trav2;
							while (trav2 != null)
							{
								if (trav2.next(ptl) == null)
								{
									trav2 = trav2.next(ptl);
								}
								else
								{
									boolean maybe = trav2.next(ptl).maybeGrow();
									if (maybe)
									{
										tallboi.Nheight.set(ptl + 1, trav2.next(ptl));
										trav2 = trav2.next(ptl);
										tallboi = trav2;
									}
									else
									{
										trav2 = trav2.next(ptl);
									}
								}
							}
						}
					}
					else
					{
						trav = trav.next(h);
					}
				}
				else
				{
					trav = trav.next(h);
				}
			}
		}
		return;
	}

	public void delete(T data)
	{
		if (head.next(0).value().compareTo(data) > 0)
			return;
		Node<T> trav = head;
		Node<T> deltrav = head;
		ArrayList<Node<T>> relink = new ArrayList<Node<T>>();
		Node<T> link;
		int h = head.height() - 1;
		int del = 0;
		while (trav != null && h >= 0)
		{
			if (trav.next(h) == null || trav.next(h).value().compareTo(data) >= 0)
			{
				if (h == 0 && trav.next(h).value().compareTo(data) == 0)
				{
					relink.add(trav);
					trav = trav.next(h);
					break;
				}
				relink.add(trav);
				h--;
			}
			else if (trav.next(h).value().compareTo(data) < 0)
			{
				trav = trav.next(h);
			}
		}
		if (h == 0 && trav != null)
		{
			Collections.reverse(relink);
			for(int i = 0; i < trav.height(); i++)
			{
				link = relink.get(i);
				link.Nheight.set(i, trav.next(i));
			}
			del = 1;
		}
		if (del == 1)
			n--;
		if (getMaxHeightdel(n) < height() && getMaxHeightdel(n) != 0 && del == 1)
		{
			while (deltrav != null)
			{
				deltrav.trim(getMaxHeightdel(n));
				deltrav = deltrav.next(deltrav.height() - 1);
			}
		}
		return;
	}

	// If get() returns a non-null reference type, the function
	// returns true
	public boolean contains(T data)
	{
		Node<T> contain = get(data);
		if(contain != null)
			return true;
		return false;
	}

	public Node<T> get(T data)
	{
		Node<T> trav = head;
		int h = head.height() - 1;
		// Searches from the top level downward to find the node
		// in the SkipList that contains data
		while (trav != null && h >= 0)
		{
			if (trav.next(h) == null || trav.next(h).value().compareTo(data) > 0)
				h--;
			else if (trav.next(h).value().compareTo(data) < 0)
				trav = trav.next(h);
			else
			{
				return trav.next(h);
			}
		}
		return null;
	}
	public static double difficultyRating()
	{
		return 5.0;
	}
	public static double hoursSpent()
	{
		return 45.0;
	}
}
