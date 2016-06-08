#!/usr/bin/env perl
#call dist.exe to produce matrix
#
use strict;
if(@ARGV<4 || (@ARGV>4 && @ARGV<7))
{
	print "$0 mem_limit(GByte) new_list.file new_cv.dir out_matrix.file \\ \n";
	print "   [exists_list.file exists_cv.dir exists_matrix.file] \n";
	print "   (please give 4 parameters or >=7 paramters.)\n";
	print "VERSION 4.0\n";
	exit(1);
}
my $CVext=".cv";
my $max_mem=$ARGV[0]*1024*1024*1024;
my $max_mem_rec=$ARGV[0];
my $new_file=$ARGV[1];
my $new_dir=$ARGV[2];
my $out_file=$ARGV[3];
my $old_file="";
my $old_dir="";
my @old_mfile=();
if(@ARGV>6)
{
	$old_file=$ARGV[4];
	$old_dir=$ARGV[5];
	@old_mfile=@ARGV[6 .. $#ARGV];
}
$new_dir=~s/\/$//;
$old_dir=~s/\/$//;

my $dist_cmd=$0;
$dist_cmd=~ s/batch_dist.pl$/dist/;

my @new_list=read_list($new_file);
my %matrix;#{file1}{file2}=dist_value
my @old_list;

if($old_file)
{
	@old_list=read_list($old_file);
	foreach my $cur_old_mfile (@old_mfile)
	{
		if(open(F,"$cur_old_mfile"))
		{
			<F>;
			my @mlist;
			while(<F>)
			{
				chomp $_;
				my @tmp =split(" ",$_);
				for(my $i=0;$i<@mlist;++$i)
				{
					$matrix{$mlist[$i]}{$tmp[0]}=$tmp[$i+1];
					$matrix{$tmp[0]}{$mlist[$i]}=$tmp[$i+1];
				}
				push @mlist,$tmp[0];
			}
			close F;
		}
	}
}

my $d_begin_i=0;
my @dblist;
my $sizesum=0;
my $rec_begin=0;
my $count_new=$#new_list+1;
my $count_old=$#old_list+1;
my $total_work=($count_new-1)*$count_new*0.5+$count_new*$count_old;
if($total_work == 0 && $count_new==1)
{
	open (OUT,">$out_file")||die $!;
	print OUT ($#new_list+1)."\n";
	foreach my $n0 (@new_list)
	{
	    printf OUT ("%-9s",$n0);
	    foreach my $n1 (@new_list)
	    {
    	    printf OUT (" %.10f",0);
	    }
	    print OUT "\n";
	}
	close OUT;
	exit;
}
print "mem limit : ".(int($max_mem*100/(1024*1024*1024))/100)." G\n";
for(my $i=0;$i<@new_list;++$i)
{
	my $newfile=$new_dir."/".$new_list[$i].$CVext;
	if(!@dblist)
	{
		$rec_begin=$i;
	}
	push @dblist,$newfile;
	if(-e "$newfile")
	{
		$sizesum += -s "$newfile";#get file size
	}
	else
	{
		die "file: $newfile does not exists.\n";
	}

	if($i==$#new_list || $sizesum>($max_mem - 100000000))#100M
	{
		#warn "dblist: ",join(",",@dblist),"\n";
		my $passed=int((($rec_begin-1)*$rec_begin*0.5+$rec_begin*($count_new-$rec_begin)+$rec_begin*$count_old)/$total_work*10000)/100;
		my $next_i=$i+1;
		my $current=int((($next_i-1)*$next_i*0.5+$next_i*($count_new-$next_i)+$next_i*$count_old)/$total_work*10000)/100;
		my $cur_m=$#dblist+1;
		my $cur_work=0.5*($cur_m-1)*$cur_m+$cur_m*($count_new-$next_i)+$cur_m*$count_old;
		print "Current round: $passed~$current%, need calulate $cur_work distance pair.\n";

		my $dbarg=join(",",@dblist);
		my @qlist=();
		for(my $j=$i+1;$j<@new_list;++$j)
		{
			my $tmpfile=$new_dir."/".$new_list[$j].$CVext;
			push @qlist,$tmpfile;
		}
		foreach(@old_list)
		{
			my $tmpfile=$old_dir."/$_$CVext";
			if(!-e "$tmpfile" )
			{
				if(-e "$tmpfile.gz")
				{
					$tmpfile.=".gz";
					push @qlist,$tmpfile;#gzip file
				}
				else
				{
					warn "$tmpfile not exists!\n";
				}
			}
			else #plain file
			{
				push @qlist,$tmpfile;
			}
		}
		my $query_qarg= join(" ",@qlist);
		my $query_qarg_num=$#qlist+1;
		#open(DIST,"$dist_cmd -d $dbarg -c $query_qarg |")|| die $!;
		#warn "zcat -f $query_qarg |$dist_cmd -d $dbarg -c -n $query_qarg_num\n";
		#my @result_dist;
		my $command;
		if($query_qarg_num)
		{
			#@result_dist =`zcat -f $query_qarg |$dist_cmd -d $dbarg -c -n $query_qarg_num`;
			$command = "zcat -f $query_qarg |$dist_cmd -d $dbarg -c -n $query_qarg_num |";
		}
		else
		{
			#@result_dist =`$dist_cmd -d $dbarg -c`;
			$command = "$dist_cmd -d $dbarg -c |";
		}
		#foreach my $d(@result_dist)
		my $passed_count=0;
		open (DIST,"$command") || die "Error: \n$command\n$!";
		$|=1;
		while(my $d=<DIST>)
		{
			chomp $d;
			#warn "$d\n";
			my @tmp=split("\t",$d);
			#warn "0=$tmp[0],1=$tmp[1]\n";
			if($tmp[0] =~ /stdin\_(\d+)/)
			{
				$tmp[0]=$qlist[$1-1];
			}
			if($tmp[1] =~ /stdin\_(\d+)/)
			{
				$tmp[1]=$qlist[$1-1];
			}
			$tmp[0]=~ s/^.*\///;
			$tmp[1]=~ s/^.*\///;

			$tmp[0]=~/(.*)$CVext/;
			my $n0=$1;
			$tmp[1]=~/(.*)$CVext/;
			my $n1=$1;

			#warn "get: $n0,$n1 =>",$tmp[2],"\n";
			$matrix{$n0}{$n1}=$matrix{$n1}{$n0}=$tmp[2];
			$passed_count++;
			print "[$passed_count/$cur_work] ".(int($passed_count/$cur_work*10000)/100);
			print "% finished in current round($passed~$current%)\n";
		}
		close DIST;
		$|=0;
		$sizesum=0;
		@dblist=();

		my $free_mem = stat_mem(8);#total 8G mem
		while($free_mem<0.1)#less than 500M free mem
		{
			print "System's free memory is less than 500M, wait for 30s automatically.\n";
			sleep(30);
			$free_mem = stat_mem(8);
		}
		if($free_mem>$max_mem_rec)
		{
			$free_mem=$max_mem_rec;
		}
		$free_mem *= 1024*1024*1024;
		$max_mem=$free_mem;
		print "mem limit adjusted to : ".(int($max_mem*100/(1024*1024*1024))/100)." G\n";
	}
}

push @new_list,@old_list;
my $max_name_len=0;
foreach(@new_list)
{
	if($max_name_len<length($_))
	{
		$max_name_len=length($_);
	}
}
if($max_name_len<9)
{
	$max_name_len=9;
}

open (OUT,">$out_file")||die $!;
print OUT ($#new_list+1)."\n";
foreach my $n0 (@new_list)
{
	printf OUT ("%-$max_name_len"."s",$n0);
	foreach my $n1 (@new_list)
	{
		printf OUT (" %.10f",$matrix{$n0}{$n1});
	}
	print OUT "\n";
}
close OUT;
###############end###############

sub read_list
{
	my($file)=@_;
	my @list;
	open(F,"$file")||die $!;
	my $num=<F>;
	$num=~/^(\d+)/;
	$num=$1;
	for(my $i=0;$i<$num;++$i)
	{
		my $line=<F>;
		$line=~/^(\S+)/;
		push @list,$1;
	}
	close F;
	return @list;
}

sub stat_mem
{
    my($total)=@_;
    my @line = `top -b -n 1 -s`;
    my %expan;
    $expan{""}=1/(1024*1024*1024);
    $expan{'k'}=1/(1024*1024);
    $expan{'m'}=1/1024;
    $expan{'g'}=1;

    my $mem=0;
    for(my $i=7;$i<17;++$i)
    {
            my @tmp=split(" ",$line[$i]);
            $tmp[5]=~/(.*?)([gmk]?)$/;
            #warn "$tmp[5]: $1,$2\n";
            $mem += $1*$expan{$2};
    }
    return int(($total*0.9-$mem)*100)/100;
}

