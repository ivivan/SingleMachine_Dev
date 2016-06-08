#!/usr/bin/env perl
use strict;
if(@ARGV<2)
{
	print "$0 single_input.fa name_list.txt\n";
	exit(1);
}

my $input=$ARGV[0];
my $output=$ARGV[1];

my @name;
my %unic;
my $count=0;
open(F,"$input")||die "open $input error\n";
while(<F>)
{
	if($_=~/^>.*\|(.*)/)
	{
		my $n=$1;
		$n=~s/^\s*//;
		$n=~s/\s*$//;
		$n=~tr/ /_/;
		if(!exists $unic{$n})
		{
			push @name, $n;
			$unic{$n}=1;
			$count++;
		}
	}
}
close F;

open(F,">$output") || die "open $output error\n";
print F $count,"\n";
foreach(@name)
{
	print F $_,"\n";
}
close F;
